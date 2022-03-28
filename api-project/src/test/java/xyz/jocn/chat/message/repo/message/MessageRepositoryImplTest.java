package xyz.jocn.chat.message.repo.message;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class MessageRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	MessageRepository repo;

	@BeforeEach
	void setUp() {
	}
}