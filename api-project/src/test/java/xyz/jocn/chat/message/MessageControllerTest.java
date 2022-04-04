package xyz.jocn.chat.message;

import org.junit.jupiter.api.BeforeEach;
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
	MessageService messageService;

	@BeforeEach
	void setUp() {
	}

	@Test
	void sendMessage() {
		// given

		// when

		// then
	}

	@Test
	void fetchMessages() {
		// given

		// when

		// then
	}

	@Test
	void deleteMessage() {
		// given

		// when

		// then
	}
}