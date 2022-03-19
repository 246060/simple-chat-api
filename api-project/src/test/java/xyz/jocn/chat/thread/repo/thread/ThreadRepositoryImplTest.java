package xyz.jocn.chat.thread.repo.thread;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.thread.repo.ThreadRepository;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ThreadRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	ThreadRepository repo;

	@BeforeEach
	void setUp() {
	}
}