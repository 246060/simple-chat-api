package xyz.jocn.chat.participant.repo.room_participant;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class RoomParticipantRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	RoomParticipantRepository repo;

	@BeforeEach
	void setUp() {
	}
}