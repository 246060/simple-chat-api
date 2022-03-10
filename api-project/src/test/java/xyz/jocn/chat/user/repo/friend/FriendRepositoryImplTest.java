package xyz.jocn.chat.user.repo.friend;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class FriendRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	FriendRepository repo;

	@BeforeEach
	void setUp() {
	}
}