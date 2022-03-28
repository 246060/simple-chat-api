package xyz.jocn.chat.channel.repo;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ChannelRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	ChannelRepository repo;
}