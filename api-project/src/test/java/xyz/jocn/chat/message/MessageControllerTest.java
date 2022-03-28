package xyz.jocn.chat.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	MessageService roomMessageService;

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
	void getMarks() {
		// given

		// when

		// then
	}

	@Test
	void deleteMark() {
		// given

		// when

		// then
	}
}