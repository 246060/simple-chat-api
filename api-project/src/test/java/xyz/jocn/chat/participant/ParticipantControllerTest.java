package xyz.jocn.chat.participant;

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

import xyz.jocn.chat.FakeToken;
import xyz.jocn.chat.participant.dto.ChannelExitDto;
import xyz.jocn.chat.participant.dto.ChannelInviteRequestDto;
import xyz.jocn.chat.participant.dto.ParticipantDto;
import xyz.jocn.chat.user.dto.UserDto;

@WebMvcTest(ParticipantController.class)
class ParticipantControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ParticipantService participantService;

	@Autowired
	ObjectMapper om;

	FakeToken testToken = new FakeToken();

	@BeforeEach
	void setUp() {
	}

	@Test
	void invite() throws Exception {
		// given
		long uid = 1L;
		long channelId = 1L;
		long targetId = 2L;
		String token = testToken.generate(uid);

		ChannelInviteRequestDto dto = new ChannelInviteRequestDto();
		dto.setInvitees(List.of(targetId));

		willDoNothing().given(participantService).invite(uid, channelId, dto);

		String jsonStr = om.writeValueAsString(dto);

		// when
		ResultActions actions =
			mockMvc.perform(post("/channels/{channelId}/participants", channelId)
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(jsonStr)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(ParticipantController.class))
			.andExpect(handler().methodName("invite"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())
			.andExpect(jsonPath("$.data").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))
		;

		then(participantService).should().invite(uid, channelId, dto);
	}

	@Test
	void fetchCurrentParticipantsInChannel() throws Exception {
		// given
		long uid = 1L;
		long channelId = 1L;
		long participantId = 1L;
		String token = testToken.generate(uid);

		UserDto userDto = new UserDto();
		userDto.setId(uid);
		userDto.setName("user01");
		userDto.setEmail("user01@test.org");
		userDto.setProfileImgUrl("/files/1");
		userDto.setStateMessage("hello world");

		ParticipantDto dto = new ParticipantDto();
		dto.setId(participantId);
		dto.setUser(userDto);

		List<ParticipantDto> dtos = new ArrayList<>();
		dtos.add(dto);

		given(participantService.fetchCurrentParticipantsInChannel(uid, channelId))
			.willReturn(dtos);

		// when
		ResultActions actions =
			mockMvc.perform(get("/channels/{channelId}/participants", channelId)
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(ParticipantController.class))
			.andExpect(handler().methodName("fetchCurrentParticipantsInChannel"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data[0].id").exists())
			.andExpect(jsonPath("$.data[0].user").exists())
			.andExpect(jsonPath("$.data[0].user.id").exists())
			.andExpect(jsonPath("$.data[0].user.name").exists())
			.andExpect(jsonPath("$.data[0].user.email").exists())
			.andExpect(jsonPath("$.data[0].user.profileImgUrl").exists())
			.andExpect(jsonPath("$.data[0].user.stateMessage").exists())
		;
	}

	@Test
	void fetchOneInChannel() throws Exception {
		// given
		long uid = 1L;
		long channelId = 1L;
		long participantId = 1L;
		String token = testToken.generate(uid);

		UserDto userDto = new UserDto();
		userDto.setId(uid);
		userDto.setName("user01");
		userDto.setEmail("user01@test.org");
		userDto.setProfileImgUrl("/files/1");
		userDto.setStateMessage("hello world");

		ParticipantDto participantDto = new ParticipantDto();
		participantDto.setId(participantId);
		participantDto.setUser(userDto);

		given(participantService.fetchOneInChannel(uid, channelId, participantId)).willReturn(participantDto);

		// when
		ResultActions actions =
			mockMvc.perform(get("/channels/{channelId}/participants/{participantId}", channelId, participantId)
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(ParticipantController.class))
			.andExpect(handler().methodName("fetchOneInChannel"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.id").exists())
			.andExpect(jsonPath("$.data.user").exists())
			.andExpect(jsonPath("$.data.user.id").exists())
			.andExpect(jsonPath("$.data.user.name").exists())
			.andExpect(jsonPath("$.data.user.email").exists())
			.andExpect(jsonPath("$.data.user.profileImgUrl").exists())
			.andExpect(jsonPath("$.data.user.stateMessage").exists())
		;

		then(participantService).should().fetchOneInChannel(uid, channelId, participantId);
	}

	@Test
	void exit() throws Exception {
		// given
		long uid = 1L;
		long channelId = 1L;
		long participantId = 1L;
		String token = testToken.generate(uid);

		willDoNothing().given(participantService).exit(any(ChannelExitDto.class));

		// when
		ResultActions actions =
			mockMvc.perform(delete("/channels/{channelId}/participants/{participantId}", channelId, participantId)
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(ParticipantController.class))
			.andExpect(handler().methodName("exit"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())
			.andExpect(jsonPath("$.data").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))
		;

		then(participantService).should().exit(any(ChannelExitDto.class));
	}

}