package xyz.jocn.chat.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import xyz.jocn.chat.auth.TokenController;
import xyz.jocn.chat.auth.TokenService;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserService userService;

	@Test
	void signUp() {
		// given

		// when

		// then
	}

	@Test
	void create() {
		// given

		// when

		// then
	}

	@Test
	void group() {
		// given

		// when

		// then
	}

	@Test
	void testGroup() {
		// given

		// when

		// then
	}

	@Test
	void blockFriendAddition() {
		// given

		// when

		// then
	}
}