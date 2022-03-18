package xyz.jocn.chat.user;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.jocn.chat.TestToken;
import xyz.jocn.chat.user.controller.UserController;
import xyz.jocn.chat.user.dto.UserDto;
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;
import xyz.jocn.chat.user.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserService userService;

	@Autowired
	ObjectMapper om;

	TestToken testToken = new TestToken();

	@Test
	void signUp() throws Exception {

		// given
		UserSignUpRequestDto dto = new UserSignUpRequestDto();
		dto.setEmail("test@hello.com");
		dto.setPassword("passwor!2Dd");

		long newUserId = 1L;

		given(userService.signUp(any(UserSignUpRequestDto.class))).willReturn(newUserId);

		String jsonStr = om.writeValueAsString(dto);

		// when
		ResultActions actions = mockMvc.perform(
			post("/users")
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(jsonStr)
		).andDo(print());

		// then
		actions
			.andExpect(status().isCreated())
			.andExpect(handler().handlerType(UserController.class))
			.andExpect(handler().methodName("signUp"))

			.andExpect(header().exists(LOCATION))
			.andExpect(header().stringValues(CONTENT_TYPE, APPLICATION_JSON_VALUE))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())
			.andExpect(jsonPath("$.data").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))
		;

		then(userService).should(times(1)).signUp(any(UserSignUpRequestDto.class));
	}

	@Test
	void me() throws Exception {
		// given
		Long userId = 1L;
		String token = testToken.generate(userId);

		UserDto dto = new UserDto();
		dto.setId(userId);

		given(userService.fetchMe(anyLong())).willReturn(dto);

		// when
		ResultActions actions = mockMvc.perform(
			get("/users/me")
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
		).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(UserController.class))
			.andExpect(handler().methodName("me"))

			.andExpect(header().stringValues(CONTENT_TYPE, APPLICATION_JSON_VALUE))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.id").exists())
			.andExpect(jsonPath("$.data.id").value(1L))
		;

		then(userService).should().fetchMe(anyLong());
	}

	@Test
	void exit() throws Exception {
		// given
		Long userId = 1L;
		String token = testToken.generate(userId);

		willDoNothing().given(userService).exit(anyLong());

		// when
		ResultActions actions = mockMvc.perform(
			delete("/users/{id}", userId)
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
		).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(UserController.class))
			.andExpect(handler().methodName("leave"))

			.andExpect(header().stringValues(CONTENT_TYPE, APPLICATION_JSON_VALUE))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())
			.andExpect(jsonPath("$.data").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))
		;

		then(userService).should(times(1)).exit(anyLong());
	}

	@Test
	void exit_ApiAccessDenyException() throws Exception {
		// given
		Long userId = 1L;
		String token = testToken.generate(2L);

		willDoNothing().given(userService).exit(anyLong());

		// when
		ResultActions actions = mockMvc.perform(
			delete("/users/{id}", userId)
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
		).andDo(print());

		// then
		actions
			.andExpect(status().isForbidden())
			.andExpect(handler().handlerType(UserController.class))
			.andExpect(handler().methodName("leave"))

			.andExpect(header().stringValues(CONTENT_TYPE, APPLICATION_JSON_VALUE))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.data").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(false))

			.andExpect(jsonPath("$.error").exists())
			.andExpect(jsonPath("$.error.code").exists())
			.andExpect(jsonPath("$.error.code").isNumber())
			.andExpect(jsonPath("$.error.code").value(403))
			.andExpect(jsonPath("$.error.description").exists())
			.andExpect(jsonPath("$.error.description").isString())
			.andExpect(jsonPath("$.error.description").value("only leave own account"))
			.andExpect(jsonPath("$.error.detail").doesNotExist())
		;

		then(userService).should(times(0)).exit(anyLong());
	}
}