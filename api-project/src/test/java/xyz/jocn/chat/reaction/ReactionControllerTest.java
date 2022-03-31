package xyz.jocn.chat.reaction;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.jocn.chat.TestToken;
import xyz.jocn.chat.reaction.dto.ReactionAddRequestDto;
import xyz.jocn.chat.reaction.dto.ReactionDto;

@WebMvcTest(ReactionController.class)
class ReactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ReactionService reactionService;

	@Autowired
	ObjectMapper om;

	TestToken testToken = new TestToken();

	@BeforeEach
	void setUp() {
	}

	@Test
	void addReaction() throws Exception {
		// given
		long uid = 1L;
		long channelId = 1L;
		long messageId = 1L;
		String token = testToken.generate(uid);

		ReactionAddRequestDto dto = new ReactionAddRequestDto();
		dto.setType(ReactionType.Agree);

		String jsonStr = om.writeValueAsString(dto);

		willDoNothing().given(reactionService).addReaction(uid, channelId, messageId, dto);

		// when
		ResultActions actions =
			mockMvc.perform(post("/channels/{channelId}/messages/{messageId}/reactions", channelId, messageId)
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(jsonStr)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(ReactionController.class))
			.andExpect(handler().methodName("addReaction"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())
			.andExpect(jsonPath("$.data").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))
		;

		then(reactionService).should().addReaction(uid, channelId, messageId, dto);
	}

	@Test
	void fetchReactions() throws Exception {
		// given
		long uid = 1L;
		long channelId = 1L;
		long messageId = 1L;
		long reactionId = 1L;
		String token = testToken.generate(uid);

		ReactionDto dto = new ReactionDto();
		dto.setId(reactionId);
		List<ReactionDto> dtos = new ArrayList<>();
		dtos.add(dto);

		given(reactionService.fetchReactions(uid, channelId, messageId)).willReturn(dtos);

		// when
		ResultActions actions =
			mockMvc.perform(get("/channels/{channelId}/messages/{messageId}/reactions", channelId, messageId)
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(ReactionController.class))
			.andExpect(handler().methodName("fetchReactions"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data[*].id").exists())
		;

		then(reactionService).should().fetchReactions(uid, channelId, messageId);
	}

	@Test
	void cancelReaction() throws Exception {
		// given
		long uid = 1L;
		long channelId = 1L;
		long messageId = 1L;
		long reactionId = 1L;
		String token = testToken.generate(uid);

		willDoNothing().given(reactionService).cancelReaction(uid, channelId, messageId, reactionId);

		// when
		ResultActions actions =
			mockMvc.perform(
				delete(
					"/channels/{channelId}/messages/{messageId}/reactions/{reactionId}"
					, channelId, messageId, reactionId
				)
					.header(AUTHORIZATION, token)
					.contentType(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(ReactionController.class))
			.andExpect(handler().methodName("cancelReaction"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())
			.andExpect(jsonPath("$.data").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))
		;

		then(reactionService).should().cancelReaction(uid, channelId, messageId, reactionId);
	}
}