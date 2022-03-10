package xyz.jocn.chat.participant.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import xyz.jocn.chat.auth.TokenController;
import xyz.jocn.chat.participant.service.RoomParticipantService;
import xyz.jocn.chat.participant.service.ThreadParticipantService;

@WebMvcTest(ThreadParticipantController.class)
class ThreadParticipantControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ThreadParticipantService threadParticipantService;

	@Test
	void create() {
		// given

		// when

		// then
	}

	@Test
	void get() {
		// given

		// when

		// then
	}

	@Test
	void get2() {
		// given

		// when

		// then
	}
}