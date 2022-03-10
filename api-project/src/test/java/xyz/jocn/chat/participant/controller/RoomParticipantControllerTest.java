package xyz.jocn.chat.participant.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import xyz.jocn.chat.auth.TokenController;
import xyz.jocn.chat.auth.TokenService;
import xyz.jocn.chat.participant.service.RoomParticipantService;

@WebMvcTest(RoomParticipantController.class)
class RoomParticipantControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	RoomParticipantService roomParticipantService;

	@Test
	void invite() {
		// given

		// when

		// then
	}

	@Test
	void getParticipants() {
		// given

		// when

		// then
	}

	@Test
	void exit() {
		// given

		// when

		// then
	}
}