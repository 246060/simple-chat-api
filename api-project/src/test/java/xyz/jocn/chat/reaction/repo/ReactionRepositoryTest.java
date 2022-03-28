package xyz.jocn.chat.reaction.repo;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ReactionRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	ReactionRepository repo;

	@BeforeEach
	void setUp() {
	}

	@Test
	void findAllByRoomMessage() {
		// given

		// when

		// then
	}
}