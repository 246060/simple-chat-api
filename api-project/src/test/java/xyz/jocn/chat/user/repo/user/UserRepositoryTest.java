package xyz.jocn.chat.user.repo.user;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.repo.UserRepository;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class UserRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	UserRepository repo;

	@BeforeEach
	void setUp() {
	}

	@Test
	void findByEmail() {
		// given
		String email = "abc@abc.org";
		UserEntity userEntity = UserEntity.builder().email(email).password("password").build();
		em.persist(userEntity);
		em.flush();
		em.clear();

		// when
		UserEntity result = repo.findByEmail(email).get();

		// then
		assertThat(result).extracting(UserEntity::getId).isNotNull();
		assertThat(result).extracting(UserEntity::getEmail).isEqualTo(userEntity.getEmail());
		assertThat(result).extracting(UserEntity::getPassword).isEqualTo(userEntity.getPassword());
	}

	@Test
	void findByIdIn() {
		// given
		List<Long> ids = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			String email = "abc" + i + "@abc.org";
			UserEntity userEntity = UserEntity.builder().email(email).password("password").build();
			em.persist(userEntity);
			ids.add(userEntity.getId());
		}
		em.flush();
		em.clear();

		// when
		List<UserEntity> users = repo.findByIdIn(ids);
		System.out.println("users = " + users);

		// then
		assertThat(users).hasSize(3);
		assertThat(users).extracting(UserEntity::getId).containsAll(ids);
	}
}