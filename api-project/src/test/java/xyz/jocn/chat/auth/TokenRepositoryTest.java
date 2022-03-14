package xyz.jocn.chat.auth;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.user.entity.UserEntity;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class TokenRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	TokenRepository repo;

	@BeforeEach
	void setUp() {
	}

	@Test
	void findByRefreshTokenAndRefreshExpireTimeAfter() {
		// given
		String refreshToken = UUID.randomUUID().toString();
		Instant now = Instant.now();

		TokenEntity entity =
			TokenEntity.builder()
				.refreshToken(refreshToken)
				.refreshExpireTime(Instant.now().plusSeconds(360000))
				.build();

		em.persist(entity);
		em.flush();
		em.clear();

		// when
		TokenEntity result = repo.findByRefreshTokenAndRefreshExpireTimeAfter(refreshToken, now).get();
		System.out.println("result = " + result);

		// then
		assertThat(result).extracting(TokenEntity::getId).isEqualTo(entity.getId());
		assertThat(result).extracting(TokenEntity::getRefreshToken).isNotNull();
		assertThat(result).extracting(TokenEntity::getRefreshToken).isEqualTo(refreshToken);
	}
}