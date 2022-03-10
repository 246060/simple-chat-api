package xyz.jocn.chat.message.repo.room_message_file;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.chat_space.repo.room.RoomRepository;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class RoomMessageFileRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	RoomMessageFileRepository repo;


	@BeforeEach
	void setUp() {
	}
}