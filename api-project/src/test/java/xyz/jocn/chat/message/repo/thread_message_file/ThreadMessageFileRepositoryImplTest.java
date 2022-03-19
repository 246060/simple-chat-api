package xyz.jocn.chat.message.repo.thread_message_file;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ThreadMessageFileRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	ThreadMessageFileRepository repo;

	@BeforeEach
	void setUp() {
	}
}