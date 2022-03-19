package xyz.jocn.chat.auth;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import xyz.jocn.chat.auth.dto.JwtClaimsSetDto;
import xyz.jocn.chat.auth.dto.TokenCreateRequestDto;
import xyz.jocn.chat.auth.dto.TokenRefreshRequestDto;
import xyz.jocn.chat.auth.dto.TokenResponseDto;
import xyz.jocn.chat.common.exception.AuthenticationException;
import xyz.jocn.chat.common.exception.TokenException;
import xyz.jocn.chat.common.util.TokenUtil;
import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.enums.UserRole;
import xyz.jocn.chat.user.repo.UserRepository;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

	@Mock
	TokenRepository tokenRepository;
	@Mock
	UserRepository userRepository;
	@Mock
	PasswordEncoder passwordEncoder;
	@Mock
	TokenUtil tokenUtil;

	@InjectMocks
	TokenService tokenService;

	@Test
	void generateToken() {
		// given
		TokenCreateRequestDto dto = new TokenCreateRequestDto();
		dto.setEmail("abc@abc.org");
		dto.setPassword("pass!@Word");

		UserEntity userEntity =
			UserEntity.builder().id(1L).role(UserRole.USER).password(UUID.randomUUID().toString()).build();

		String accessToken = UUID.randomUUID().toString();
		String refreshToken = UUID.randomUUID().toString();

		int AccessTokenExpireInSec = 3600000;
		int refreshTokenExpireInSec = 360000000;

		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(userEntity));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

		given(tokenUtil.generateJwt(any(JwtClaimsSetDto.class))).willReturn(accessToken);
		given(tokenUtil.generateRefreshToken()).willReturn(refreshToken);
		given(tokenUtil.getRefreshTokenExpireInSec()).willReturn(refreshTokenExpireInSec);
		given(tokenUtil.getAccessTokenExpireInSec()).willReturn(AccessTokenExpireInSec);

		TokenEntity tokenEntity = TokenEntity.builder().refreshToken(refreshToken).build();
		given(tokenRepository.save(any(TokenEntity.class))).willReturn(tokenEntity);

		// when
		TokenResponseDto result = tokenService.generateToken(dto);

		// then
		then(userRepository).should().findByEmail(anyString());
		then(tokenRepository).should().save(any(TokenEntity.class));
		then(passwordEncoder).should(times(1)).matches(anyString(), anyString());

		then(tokenUtil).should(times(1)).generateJwt(any(JwtClaimsSetDto.class));
		then(tokenUtil).should(times(1)).generateRefreshToken();
		then(tokenUtil).should(times(1)).getRefreshTokenExpireInSec();
		then(tokenUtil).should(times(1)).getAccessTokenExpireInSec();

		assertThat(result).extracting(TokenResponseDto::getAccessToken).isNotNull();
		assertThat(result).extracting(TokenResponseDto::getAccessToken).isEqualTo(accessToken);
		assertThat(result).extracting(TokenResponseDto::getExpiresIn).isEqualTo(AccessTokenExpireInSec);
		assertThat(result).extracting(TokenResponseDto::getRefreshToken).isNotNull();
		assertThat(result).extracting(TokenResponseDto::getRefreshToken).isEqualTo(refreshToken);
	}

	@Test
	void generateToken_AuthenticationException() {
		// given
		TokenCreateRequestDto dto = new TokenCreateRequestDto();
		dto.setEmail("abc@abc.org");
		dto.setPassword("password");

		UserEntity user = UserEntity.builder().password(UUID.randomUUID().toString()).build();
		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

		// when + then
		assertThatThrownBy(() -> tokenService.generateToken(dto)).isInstanceOf(AuthenticationException.class);

		then(userRepository).should(times(1)).findByEmail(anyString());
		then(passwordEncoder).should(times(1)).matches(anyString(), anyString());
	}

	@Test
	void refresh() {
		// given
		UserEntity user = UserEntity.builder().id(1L).role(UserRole.USER).build();
		TokenEntity tokenEntity = TokenEntity.builder().user(user).build();

		given(tokenRepository.findByRefreshTokenAndRefreshExpireTimeAfter(anyString(), any(Instant.class)))
			.willReturn(Optional.of(tokenEntity));

		String refreshToken = UUID.randomUUID().toString();
		String accessToken = UUID.randomUUID().toString();
		int exp = 36000;

		given(tokenUtil.generateJwt(any(JwtClaimsSetDto.class))).willReturn(accessToken);
		given(tokenUtil.getAccessTokenExpireInSec()).willReturn(exp);

		TokenRefreshRequestDto dto = new TokenRefreshRequestDto();
		dto.setRefreshToken(refreshToken);

		// when
		TokenResponseDto result = tokenService.refresh(dto);

		// then
		then(tokenRepository)
			.should(times(1))
			.findByRefreshTokenAndRefreshExpireTimeAfter(anyString(), any(Instant.class));

		assertThat(result).extracting(TokenResponseDto::getAccessToken).isNotNull();
		assertThat(result).extracting(TokenResponseDto::getAccessToken).isEqualTo(accessToken);
		assertThat(result).extracting(TokenResponseDto::getExpiresIn).isEqualTo(exp);
	}

	@Test
	void refresh_TokenException() {
		// given
		given(tokenRepository.findByRefreshTokenAndRefreshExpireTimeAfter(anyString(), any(Instant.class)))
			.willReturn(Optional.empty());

		TokenRefreshRequestDto dto = new TokenRefreshRequestDto();
		dto.setRefreshToken(UUID.randomUUID().toString());

		// when + then
		assertThatThrownBy(() -> tokenService.refresh(dto)).isInstanceOf(TokenException.class);

		then(tokenRepository)
			.should(times(1))
			.findByRefreshTokenAndRefreshExpireTimeAfter(anyString(), any(Instant.class));
	}
}