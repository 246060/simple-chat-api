package xyz.jocn.chat.message.repo.thread_message_reaction;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ThreadMessageMarkRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	ThreadMessageReactionRepository repo;

	@BeforeEach
	void setUp() {
	}
}