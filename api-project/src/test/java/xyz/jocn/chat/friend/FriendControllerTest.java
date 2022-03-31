package xyz.jocn.chat.friend;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.jocn.chat.TestToken;
import xyz.jocn.chat.friend.dto.FriendDto;
import xyz.jocn.chat.friend.dto.FriendRequestDto;
import xyz.jocn.chat.friend.dto.FriendSearchDto;

@WebMvcTest(FriendController.class)
class FriendControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	FriendService friendService;
	@Autowired
	ObjectMapper om;

	TestToken testToken = new TestToken();

	@BeforeEach
	void setUp() {
	}

	@Test
	void addFriend() throws Exception {

		// given
		Long hostId = 1L;
		String token = testToken.generate(hostId);

		FriendRequestDto dto = new FriendRequestDto();
		dto.setTargetEmail("user01@test.org");

		String jsonStr = om.writeValueAsString(dto);

		given(friendService.addFriend(hostId, dto)).willReturn(1L);

		// when
		ResultActions actions =
			mockMvc.perform(post("/friends")
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(jsonStr)
			).andDo(print());

		// then
		actions
			.andExpect(status().isCreated())
			.andExpect(handler().handlerType(FriendController.class))
			.andExpect(handler().methodName("addFriend"))
			.andExpect(header().exists("Location"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())
			.andExpect(jsonPath("$.data").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))
		;

		then(friendService).should().addFriend(hostId, dto);
	}

	@Test
	void fetchOne() throws Exception {
		// given
		Long uid = 1L;
		Long friendId = 1L;

		String token = testToken.generate(uid);

		FriendDto dto = new FriendDto();
		dto.setName("user01");
		dto.setHidden(false);
		dto.setFavorite(false);

		given(friendService.fetchOne(uid, friendId)).willReturn(dto);

		// when
		ResultActions actions =
			mockMvc.perform(get("/friends/{friendId}", friendId)
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(FriendController.class))
			.andExpect(handler().methodName("fetchOne"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.name").exists())
			.andExpect(jsonPath("$.data.favorite").exists())
			.andExpect(jsonPath("$.data.hidden").exists())
		;

		then(friendService).should().fetchOne(uid, friendId);
	}

	@Test
	void fetchFriends() throws Exception {
		// given
		long uid = 1L;
		String token = testToken.generate(uid);

		Boolean favorite = true;
		Boolean hidden = true;

		FriendDto friendDto = new FriendDto();
		friendDto.setName("user01");
		friendDto.setFavorite(favorite);
		friendDto.setHidden(hidden);

		List<FriendDto> friendDtos = new ArrayList<>();
		friendDtos.add(friendDto);

		given(friendService.fetchFriends(anyLong(), any(FriendSearchDto.class))).willReturn(friendDtos);

		MultiValueMap queryParam = new LinkedMultiValueMap();
		queryParam.set("favorite", String.valueOf(favorite));
		queryParam.set("hidden", String.valueOf(hidden));

		// when
		ResultActions actions =
			mockMvc.perform(get("/friends")
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.queryParams(queryParam)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(FriendController.class))
			.andExpect(handler().methodName("fetchFriends"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data[*].name").exists())
			.andExpect(jsonPath("$.data[*].favorite").exists())
			.andExpect(jsonPath("$.data[*].hidden").exists())
		;

		then(friendService).should(times(1)).fetchFriends(anyLong(), any(FriendSearchDto.class));
	}

	@Test
	void updateFriend() throws Exception {
		// given
		long uid = 1L;
		long friendId = 1L;
		String token = testToken.generate(uid);

		FriendDto friendDto = new FriendDto();
		friendDto.setHidden(true);
		friendDto.setFavorite(true);
		friendDto.setName("user01");

		given(friendService.updateFriend(uid, friendId, friendDto)).willReturn(friendDto);

		String jsonStr = om.writeValueAsString(friendDto);

		// when
		ResultActions actions =
			mockMvc.perform(patch("/friends/{friendId}", friendId)
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(jsonStr)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(FriendController.class))
			.andExpect(handler().methodName("updateFriend"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.name").exists())
			.andExpect(jsonPath("$.data.favorite").exists())
			.andExpect(jsonPath("$.data.hidden").exists())
		;

		then(friendService).should().updateFriend(uid, friendId, friendDto);
	}

	@Test
	void deleteFriend() throws Exception {
		// given
		long uid = 1L;
		long friendId = 1L;
		String token = testToken.generate(uid);

		willDoNothing().given(friendService).deleteFriend(uid, friendId);

		// when
		ResultActions actions =
			mockMvc.perform(delete("/friends/{friendId}", friendId)
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(FriendController.class))
			.andExpect(handler().methodName("deleteFriend"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())
			.andExpect(jsonPath("$.data").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))
		;

		then(friendService).should(times(1)).deleteFriend(uid, friendId);
	}
}