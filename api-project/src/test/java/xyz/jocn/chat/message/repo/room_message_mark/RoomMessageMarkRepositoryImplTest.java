package xyz.jocn.chat.message.repo.room_message_mark;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class RoomMessageMarkRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	RoomMessageMarkRepository repo;

	@BeforeEach
	void setUp() {
	}
}