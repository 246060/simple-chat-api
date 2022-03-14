package xyz.jocn.chat.auth;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import antlr.Token;
import xyz.jocn.chat.auth.dto.JwtClaimsSetDto;
import xyz.jocn.chat.auth.dto.TokenCreateRequestDto;
import xyz.jocn.chat.auth.dto.TokenRefreshRequestDto;
import xyz.jocn.chat.auth.dto.TokenResponseDto;
import xyz.jocn.chat.common.util.TokenUtil;
import xyz.jocn.chat.user.enums.UserRole;

@Import(TokenUtil.class)
@WebMvcTest(TokenController.class)
class TokenControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	TokenService tokenService;

	@Autowired
	ObjectMapper om;

	@Autowired
	TokenUtil tokenUtil;

	@Test
	void generate() throws Exception {
		// given
		TokenCreateRequestDto dto = new TokenCreateRequestDto();
		dto.setEmail("abc@abc.org");
		dto.setPassword("pass1@Wword");

		JwtClaimsSetDto claims = JwtClaimsSetDto.builder()
			.subject(String.valueOf(1L))
			.scope(Set.of(UserRole.USER.name()))
			.build();

		TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
			.accessToken(tokenUtil.generateJwt(claims))
			.expiresIn(tokenUtil.getAccessTokenExpireInSec())
			.refreshToken(tokenUtil.generateRefreshToken())
			.build();

		given(tokenService.generateToken(any(TokenCreateRequestDto.class)))
			.willReturn(tokenResponseDto);

		String jsonStr = om.writeValueAsString(dto);


		// when
		ResultActions actions = mockMvc.perform(post("/token")
			.contentType(APPLICATION_JSON)
			.accept(APPLICATION_JSON)
			.content(jsonStr)
		).andDo(print());


		// then
		actions
			.andExpect(status().isCreated())
			.andExpect(handler().handlerType(TokenController.class))
			.andExpect(handler().methodName("generate"))
			.andExpect(header().stringValues(CACHE_CONTROL, "no-store"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data").isMap())

			.andExpect(jsonPath("$.data.token_type").exists())
			.andExpect(jsonPath("$.data.token_type").isString())

			.andExpect(jsonPath("$.data.access_token").exists())
			.andExpect(jsonPath("$.data.access_token").isString())

			.andExpect(jsonPath("$.data.expires_in").exists())
			.andExpect(jsonPath("$.data.expires_in").isNumber())

			.andExpect(jsonPath("$.data.refresh_token").exists())
			.andExpect(jsonPath("$.data.refresh_token").isString())
		;
	}

	@Test
	void refresh() throws Exception {
		// given
		TokenRefreshRequestDto dto = new TokenRefreshRequestDto();
		dto.setRefreshToken(tokenUtil.generateRefreshToken());

		JwtClaimsSetDto claims = JwtClaimsSetDto.builder()
			.subject(String.valueOf(1L))
			.scope(Set.of(UserRole.USER.name()))
			.build();

		TokenResponseDto tokenResponseDto = TokenResponseDto.builder()
			.accessToken(tokenUtil.generateJwt(claims))
			.expiresIn(tokenUtil.getAccessTokenExpireInSec())
			.build();

		given(tokenService.refresh(any(TokenRefreshRequestDto.class)))
			.willReturn(tokenResponseDto);

		String jsonStr = om.writeValueAsString(dto);

		// when
		ResultActions actions = mockMvc.perform(post("/token/refresh")
			.contentType(APPLICATION_JSON)
			.accept(APPLICATION_JSON)
			.content(jsonStr)
		).andDo(print());

		// then
		actions
			.andExpect(status().isCreated())
			.andExpect(handler().handlerType(TokenController.class))
			.andExpect(handler().methodName("refresh"))
			.andExpect(header().stringValues(CACHE_CONTROL, "no-store"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data").isMap())

			.andExpect(jsonPath("$.data.token_type").exists())
			.andExpect(jsonPath("$.data.token_type").isString())

			.andExpect(jsonPath("$.data.access_token").exists())
			.andExpect(jsonPath("$.data.access_token").isString())

			.andExpect(jsonPath("$.data.expires_in").exists())
			.andExpect(jsonPath("$.data.expires_in").isNumber())
		;
	}
}