package xyz.jocn.chat.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TokenController.class)
class TokenControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	TokenService tokenService;

	@Test
	void generate() {
		// given

		// when

		// then
	}

	@Test
	void refresh() {
		// given

		// when

		// then
	}
}