package xyz.jocn.chat.room;

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

import xyz.jocn.chat.TestToken;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.room.RoomController;
import xyz.jocn.chat.room.RoomService;
import xyz.jocn.chat.room.dto.RoomCreateRequestDto;
import xyz.jocn.chat.room.dto.RoomDto;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	MessagePublisher messagePublisher;

	@MockBean
	RoomService roomService;

	@Autowired
	ObjectMapper om;

	TestToken testToken = new TestToken();

	@Test
	void open() throws Exception {
		// given
		Long hostId = 1L;
		Long inviteeId = 2L;
		String token = testToken.generate(hostId);

		RoomDto roomDto = new RoomDto();
		roomDto.setRoomId(1L);

		given(roomService.open(anyLong(), anyLong())).willReturn(roomDto);

		RoomCreateRequestDto dto = new RoomCreateRequestDto();
		dto.setInviteeId(inviteeId);
		String jsonStr = om.writeValueAsString(dto);

		// when
		ResultActions actions =
			mockMvc.perform(post("/rooms")
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(jsonStr)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(RoomController.class))
			.andExpect(handler().methodName("open"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data.roomId").exists())
			.andExpect(jsonPath("$.data.roomId").value(roomDto.getRoomId()))
		;

		then(roomService)
			.should(times(1))
			.open(anyLong(), anyLong());
	}

	@Test
	void getRooms() throws Exception {
		// given
		Long hostId = 1L;
		String token = testToken.generate(hostId);

		List<RoomDto> roomDtos = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			RoomDto roomDto = new RoomDto();
			roomDto.setRoomId(1L + i);
			roomDtos.add(roomDto);
		}

		given(roomService.fetchMyRooms(anyLong())).willReturn(roomDtos);

		// when
		ResultActions actions =
			mockMvc.perform(get("/rooms")
				.header(AUTHORIZATION, token)
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
			).andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(handler().handlerType(RoomController.class))
			.andExpect(handler().methodName("getRooms"))

			.andExpect(jsonPath("$.meta").doesNotExist())
			.andExpect(jsonPath("$.error").doesNotExist())

			.andExpect(jsonPath("$.success").exists())
			.andExpect(jsonPath("$.success").isBoolean())
			.andExpect(jsonPath("$.success").value(true))

			.andExpect(jsonPath("$.data").exists())
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data[*].roomId").exists())
		;

		then(roomService)
			.should(times(1))
			.fetchMyRooms(anyLong());
	}
}