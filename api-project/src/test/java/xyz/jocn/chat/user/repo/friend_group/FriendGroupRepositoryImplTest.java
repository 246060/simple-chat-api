package xyz.jocn.chat.user.repo.friend_group;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class FriendGroupRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	FriendGroupRepository repo;

	@BeforeEach
	void setUp() {
	}
}