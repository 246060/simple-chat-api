package xyz.jocn.chat.message.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import xyz.jocn.chat.auth.TokenController;
import xyz.jocn.chat.auth.TokenService;
import xyz.jocn.chat.message.service.ThreadMessageService;

@WebMvcTest(ThreadMessageController.class)
class ThreadMessageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ThreadMessageService threadMessageService;

	@Test
	void send() {
		// given

		// when

		// then
	}

	@Test
	void getMessages() {
		// given

		// when

		// then
	}

	@Test
	void changeMessage() {
		// given

		// when

		// then
	}

	@Test
	void mark() {
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

	@Test
	void pu3t() {
		// given

		// when

		// then
	}
}