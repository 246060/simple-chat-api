package xyz.jocn.chat.room.repo;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class RoomRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	RoomRepository repo;
}