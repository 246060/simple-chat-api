package xyz.jocn.chat.chat_space.repo.room;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.user.repo.user.UserRepository;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class RoomRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	RoomRepository repo;

	@BeforeEach
	void setUp() {
	}
}