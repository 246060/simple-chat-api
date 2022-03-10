package xyz.jocn.chat.message.repo.thread_message;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ThreadMessageRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	ThreadMessageRepository repo;

	@BeforeEach
	void setUp() {
	}

	@Test
	void findByThread() {
		// given

		// when

		// then
	}
}