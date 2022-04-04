package xyz.jocn.chat.channel;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.jocn.chat.FakeToken;
import xyz.jocn.chat.channel.dto.ChannelDto;
import xyz.jocn.chat.channel.dto.ChannelOpenRequestDto;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.participant.dto.ParticipantDto;
import xyz.jocn.chat.user.dto.UserDto;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	MessagePublisher messagePublisher;
	@MockBean
	ChannelService roomService;
	@Autowired
	ObjectMapper om;

	FakeToken testToken = new FakeToken();

	@Test
	void open() throws Exception {
		// given
		Long hostId = 1L;
		Long inviteeId = 2L;
		String token = testToken.generate(hostId);

		ChannelDto channelDto = new ChannelDto();
		channelDto.setId(1L);

		given(roomService.open(anyLong(), anyLong())).willReturn(channelDto.getId());

		ChannelOpenRequestDto dto = new ChannelOpenRequestDto();
		dto.setInviteeId(inviteeId);
		String jsonStr = om.writeValueAsString(dto);

		// when
		ResultActions actions =
			mockMvc.perform(post("/channels")
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(jsonStr)
			).andDo(print());

		// then
		actions
			.andExpect(status().isCreated())
			.andExpect(handler().handlerType(ChannelController.class))
			.andExpect(handler().methodName("open"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())
			.andExpect(jsonPath("$.data").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))
		;

		then(roomService).should(times(1)).open(anyLong(), anyLong());
	}

	@Test
	void fetchMyChannels() throws Exception {
		// given
		Long hostId = 1L;
		String token = testToken.generate(hostId);

		List<ChannelDto> channelDtos = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			ChannelDto channelDto = new ChannelDto();
			channelDto.setId(1L + i);
			channelDtos.add(channelDto);

			List<ParticipantDto> participantDtos = new ArrayList<>();
			for (int j = 0; j < 3; j++) {
				UserDto userDto = new UserDto();
				userDto.setId(1L + i);
				userDto.setName(String.format("user%d", 1L + i));
				userDto.setEmail(String.format("user%d@test.org", 1L + i));

				ParticipantDto participantDto = new ParticipantDto();
				participantDto.setId(1L + i);
				participantDto.setUser(userDto);

				participantDtos.add(participantDto);
			}

			channelDto.setParticipants(participantDtos);
		}

		given(roomService.fetchMyChannels(anyLong())).willReturn(channelDtos);

		// when
		ResultActions actions =
			mockMvc.perform(get("/channels")
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(ChannelController.class))
			.andExpect(handler().methodName("fetchMyChannels"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data[*].id").exists())
			.andExpect(jsonPath("$.data[*].participants").exists())
			.andExpect(jsonPath("$.data[*].participants").isArray())
			.andExpect(jsonPath("$.data[*].participants[*].id").exists())
			.andExpect(jsonPath("$.data[*].participants[*].user").exists())
			.andExpect(jsonPath("$.data[*].participants[*].user.id").exists())
			.andExpect(jsonPath("$.data[*].participants[*].user.name").exists())
			.andExpect(jsonPath("$.data[*].participants[*].user.email").exists())
			.andExpect(jsonPath("$.data[*].participants[*].user.profileImgUrl").exists())
			.andExpect(jsonPath("$.data[*].participants[*].user.stateMessage").exists())
		;

		then(roomService)
			.should(times(1))
			.fetchMyChannels(anyLong());
	}
}