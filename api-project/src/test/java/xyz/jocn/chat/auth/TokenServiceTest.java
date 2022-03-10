package xyz.jocn.chat.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

	@Mock
	TokenRepository repo;

	@InjectMocks
	TokenService service;

	@Test
	void generateToken() {
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