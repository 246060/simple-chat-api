package xyz.jocn.chat.user.repo.user;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class UserRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	UserRepository repo;

	@BeforeEach
	void setUp() {
	}

	@Test
	void findByEmail() {
		// given

		// when

		// then
	}

	@Test
	void findByIdIn() {
		// given

		// when

		// then
	}
}