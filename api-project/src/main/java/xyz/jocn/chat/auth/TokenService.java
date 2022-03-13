package xyz.jocn.chat.auth;

import static xyz.jocn.chat.common.exception.TokenErrorCode.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.auth.dto.JwtClaimsSetDto;
import xyz.jocn.chat.auth.dto.TokenCreateRequestDto;
import xyz.jocn.chat.auth.dto.TokenRefreshRequestDto;
import xyz.jocn.chat.auth.dto.TokenResponseDto;
import xyz.jocn.chat.common.exception.AuthenticationException;
import xyz.jocn.chat.common.exception.TokenException;
import xyz.jocn.chat.common.util.TokenUtil;
import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.repo.user.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TokenService {

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuer;

	private final TokenRepository tokenRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenUtil tokenUtil;

	@Transactional
	public TokenResponseDto generateToken(TokenCreateRequestDto tokenCreateRequestDto) {

		UserEntity userEntity = userRepository.findByEmail(tokenCreateRequestDto.getEmail())
			.filter(user -> passwordEncoder.matches(tokenCreateRequestDto.getPassword(), user.getPassword()))
			.orElseThrow(AuthenticationException::new);

		Instant now = Instant.now();

		String accessToken = tokenUtil.generateJwt(
			JwtClaimsSetDto
				.builder()
				.subject(String.valueOf(userEntity.getId()))
				.expirationTime(now.plusSeconds(tokenUtil.getAccessTokenExpireInSec()))
				.issuer(issuer)
				.scope(Set.of("USER"))
				.build()
		);

		TokenEntity tokenEntity = TokenEntity.builder()
			.user(userEntity)
			.refreshToken(tokenUtil.generateRefreshToken())
			.refreshExpireTime(now.plusSeconds(tokenUtil.getRefreshTokenExpireInSec()))
			.build();

		tokenRepository.save(tokenEntity);

		return TokenResponseDto.builder()
			.accessToken(accessToken)
			.expiresIn(tokenUtil.getAccessTokenExpireInSec())
			.refreshToken(tokenEntity.getRefreshToken())
			.build();
	}

	@Transactional
	public TokenResponseDto refresh(TokenRefreshRequestDto tokenRefreshRequestDto) {

		TokenEntity tokenEntity = tokenRepository.findByRefreshTokenAndRefreshExpireTimeAfter(
			tokenRefreshRequestDto.getRefreshToken(),
			Instant.now()
		).orElseThrow(() -> new TokenException(NotExistRefreshToken));

		Instant now = Instant.now();

		String accessToken = tokenUtil.generateJwt(JwtClaimsSetDto.builder()
			.subject(String.valueOf(tokenEntity.getUser().getId()))
			.expirationTime(now.plusSeconds(tokenUtil.getAccessTokenExpireInSec()))
			.build());

		return TokenResponseDto.builder()
			.accessToken(accessToken)
			.expiresIn(tokenUtil.getAccessTokenExpireInSec())
			.build();
	}
}
