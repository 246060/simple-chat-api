package xyz.jocn.chat.user;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.jocn.chat.FakeToken;
import xyz.jocn.chat.user.dto.UserDto;
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;
import xyz.jocn.chat.user.dto.UserUpdateRequestDto;

@WebMvcTest(UserController.class)
class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	UserService userService;
	@Autowired
	ObjectMapper om;

	FakeToken testToken = new FakeToken();

	@BeforeEach
	void setUp() {
	}

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
	void fetchMe() throws Exception {
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
			.andExpect(handler().methodName("fetchMe"))

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
			delete("/users/me")
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
		).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(UserController.class))
			.andExpect(handler().methodName("exit"))

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
	void updateMe() throws Exception {
		// given
		Long userId = 1L;
		String token = testToken.generate(userId);

		UserDto userDto = new UserDto();
		userDto.setId(userId);
		userDto.setEmail("user@test.org");
		userDto.setName("user02");
		userDto.setProfileImgUrl("/files/1");
		userDto.setStateMessage("change message");

		willDoNothing().given(userService).updateMe(anyLong(), any(UserUpdateRequestDto.class));

		UserUpdateRequestDto dto = new UserUpdateRequestDto();
		dto.setName("user02");
		dto.setStateMessage("change message");
		String jsonStr = om.writeValueAsString(dto);

		// when
		ResultActions actions = mockMvc.perform(
			patch("/users/me")
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(jsonStr)
		).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(UserController.class))
			.andExpect(handler().methodName("updateMe"))
			.andExpect(header().stringValues(CONTENT_TYPE, APPLICATION_JSON_VALUE))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.id").exists())
			.andExpect(jsonPath("$.data.email").exists())
			.andExpect(jsonPath("$.data.name").exists())
			.andExpect(jsonPath("$.data.profileImgUrl").exists())
			.andExpect(jsonPath("$.data.stateMessage").exists())
		;

		then(userService).should(times(1)).updateMe(anyLong(), any(UserUpdateRequestDto.class));
	}

	@Test
	void updateProfileImg() throws Exception {
		// given
		Long userId = 1L;
		String token = testToken.generate(userId);

		MockMultipartFile file = new MockMultipartFile(
			"file",
			"hello.txt",
			MediaType.TEXT_PLAIN_VALUE,
			"Hello, World!".getBytes()
		);

		willDoNothing().given(userService).updateProfileImg(anyLong(), any(MultipartFile.class));

		// when
		ResultActions actions = mockMvc.perform(
			multipart("/users/me/profile-image")
				.file(file)
				.with(request -> {
					request.setMethod(HttpMethod.PATCH.name());
					return request;
				})
				.header(AUTHORIZATION, token)
				.contentType(MULTIPART_FORM_DATA)
				.accept(APPLICATION_JSON)
		).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(UserController.class))
			.andExpect(handler().methodName("updateProfileImg"))
			.andExpect(header().stringValues(CONTENT_TYPE, APPLICATION_JSON_VALUE))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())
			.andExpect(jsonPath("$.data").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))
		;

		then(userService)
			.should(times(1))
			.updateProfileImg(anyLong(), any(MultipartFile.class));
	}
}