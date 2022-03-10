package xyz.jocn.chat.participant.repo.thread_participant;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ThreadParticipantRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	ThreadParticipantRepository repo;

	@BeforeEach
	void setUp() {
	}
}