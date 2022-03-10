package xyz.jocn.chat.participant.repo.thread_participant;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ThreadParticipantRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	ThreadParticipantRepository repo;

	@BeforeEach
	void setUp() {
	}

	@Test
	void findByThreadAndUser() {
		// given

		// when

		// then
	}

	@Test
	void findAllByUserId() {
		// given

		// when

		// then
	}

	@Test
	void deleteAllByUserIdAndThreadIn() {
		// given

		// when

		// then
	}
}