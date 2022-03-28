package xyz.jocn.chat.participant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ParticipantController.class)
class ParticipantControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ParticipantService roomParticipantService;

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