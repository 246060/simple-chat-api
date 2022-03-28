package xyz.jocn.chat.user.repo;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.user.repo.UserRepository;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class UserRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	UserRepository repo;

	@BeforeEach
	void setUp() {
	}
}