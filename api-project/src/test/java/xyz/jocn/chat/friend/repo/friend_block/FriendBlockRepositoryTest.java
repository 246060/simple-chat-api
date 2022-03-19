package xyz.jocn.chat.friend.repo.friend_block;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class FriendBlockRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	FriendBlockRepository repo;

	@BeforeEach
	void setUp() {
	}
}