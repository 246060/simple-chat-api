package xyz.jocn.chat.room.room;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.room.repo.RoomRepository;

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