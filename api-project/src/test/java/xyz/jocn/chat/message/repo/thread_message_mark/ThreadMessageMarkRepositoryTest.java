package xyz.jocn.chat.message.repo.thread_message_mark;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ThreadMessageMarkRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	ThreadMessageMarkRepository repo;

	@BeforeEach
	void setUp() {
	}

	@Test
	void findAllByThreadMessage() {
		// given

		// when

		// then
	}
}