package xyz.jocn.chat.auth;

import static java.time.Instant.*;
import static xyz.jocn.chat.common.exception.TokenErrorCode.*;

import java.util.Set;

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
import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.repo.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TokenService {

	private final TokenRepository tokenRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenUtil tokenUtil;

	@Transactional
	public TokenResponseDto generateToken(TokenCreateRequestDto tokenCreateRequestDto) {

		UserEntity userEntity =
			userRepository
				.findByEmail(tokenCreateRequestDto.getEmail())
				.filter(user -> passwordEncoder.matches(tokenCreateRequestDto.getPassword(), user.getPassword()))
				.orElseThrow(AuthenticationException::new);

		JwtClaimsSetDto claims = JwtClaimsSetDto.builder()
			.subject(String.valueOf(userEntity.getId()))
			.scope(Set.of(userEntity.getRole().name()))
			.build();

		TokenEntity tokenEntity = TokenEntity.builder()
			.user(userEntity)
			.refreshToken(tokenUtil.generateRefreshToken())
			.refreshExpireTime(claims.getIssueTime().plusSeconds(tokenUtil.getRefreshTokenExpireInSec()))
			.build();

		tokenRepository.save(tokenEntity);

		return TokenResponseDto.builder()
			.accessToken(tokenUtil.generateJwt(claims))
			.expiresIn(tokenUtil.getAccessTokenExpireInSec())
			.refreshToken(tokenEntity.getRefreshToken())
			.build();
	}

	@Transactional
	public TokenResponseDto refresh(TokenRefreshRequestDto tokenRefreshRequestDto) {

		TokenEntity tokenEntity =
			tokenRepository.findByRefreshTokenAndRefreshExpireTimeAfter(
				tokenRefreshRequestDto.getRefreshToken(), now()
			).orElseThrow(() -> new TokenException(NotExistRefreshToken));

		JwtClaimsSetDto claims = JwtClaimsSetDto.builder()
			.subject(String.valueOf(tokenEntity.getUser().getId()))
			.scope(Set.of(tokenEntity.getUser().getRole().name()))
			.build();

		return TokenResponseDto.builder()
			.accessToken(tokenUtil.generateJwt(claims))
			.expiresIn(tokenUtil.getAccessTokenExpireInSec())
			.build();
	}
}
