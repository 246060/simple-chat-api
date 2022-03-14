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
import xyz.jocn.chat.user.dto.UserDto;
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserService userService;

	@Autowired
	private ObjectMapper om;

	TestToken testToken = new TestToken();

	@Test
	void signUp() throws Exception {

		// given
		UserSignUpRequestDto dto = new UserSignUpRequestDto();
		dto.setEmail("test@hello.com");
		dto.setPassword("passwor!2Dd");

		willDoNothing()
			.given(userService)
			.signUp(any(UserSignUpRequestDto.class))
		;

		String jsonStr = om.writeValueAsString(dto);

		// when
		ResultActions actions =
			mockMvc.perform(post("/users")
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(jsonStr)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(UserController.class))
			.andExpect(handler().methodName("signUp"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())
			.andExpect(jsonPath("$.data").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))
		;

		then(userService)
			.should(times(1))
			.signUp(any(UserSignUpRequestDto.class))
		;
	}

	@Test
	void me() throws Exception {
		// given
		Long userId = 1L;
		String token = testToken.generate(userId);

		UserDto dto = new UserDto();
		dto.setId(userId);

		given(userService.getUser(anyLong())).willReturn(dto);

		// when
		ResultActions actions =
			mockMvc.perform(get("/users/me")
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(UserController.class))
			.andExpect(handler().methodName("me"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.id").exists())
			.andExpect(jsonPath("$.data.id").value(1L))
		;

		then(userService)
			.should()
			.getUser(anyLong())
		;
	}

	@Test
	void withdrawal() throws Exception {
		// given
		Long userId = 1L;
		String token = testToken.generate(userId);

		willDoNothing()
			.given(userService)
			.withdrawal(anyLong());

		given(userService.isNotResourceOwner(anyLong(), anyLong()))
			.willReturn(false);

		// when
		ResultActions actions =
			mockMvc.perform(delete("/users/{id}", userId)
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(UserController.class))
			.andExpect(handler().methodName("withdrawal"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())
			.andExpect(jsonPath("$.data").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))
		;

		then(userService)
			.should(times(1))
			.withdrawal(anyLong())
		;
	}
}