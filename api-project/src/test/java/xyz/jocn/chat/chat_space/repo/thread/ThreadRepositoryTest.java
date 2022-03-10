package xyz.jocn.chat.chat_space.repo.thread;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ThreadRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	ThreadRepository repo;

	@BeforeEach
	void setUp() {
	}

	@Test
	void findAllByRoomId() {
		// given

		// when

		// then
	}
}