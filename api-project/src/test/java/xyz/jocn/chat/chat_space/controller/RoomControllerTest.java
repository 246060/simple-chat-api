package xyz.jocn.chat.chat_space.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import xyz.jocn.chat.auth.TokenController;
import xyz.jocn.chat.auth.TokenService;
import xyz.jocn.chat.chat_space.service.RoomService;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	RoomService roomService;

	@Test
	void open() {
		// given

		// when

		// then
	}

	@Test
	void getRooms() {
		// given

		// when

		// then
	}
}