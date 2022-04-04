package xyz.jocn.chat.friend.repo;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.friend.FriendEntity;
import xyz.jocn.chat.friend.dto.FriendSearchDto;
import xyz.jocn.chat.user.entity.UserEntity;

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

	@Test
	void findAllBySourceIdAndCondition() {
		// given
		UserEntity source = UserEntity.builder().name("user00").email("user00@jocn.xyz").build();
		UserEntity target = UserEntity.builder().name("user01").email("user01@jocn.xyz").build();
		em.persist(source);
		em.persist(target);

		FriendEntity friendEntity = FriendEntity.builder().source(source).target(target).build();
		em.persist(friendEntity);

		em.flush();
		em.clear();

		FriendSearchDto condition = new FriendSearchDto();
		condition.setFavorite(false);
		condition.setHidden(false);

		// when
		List<FriendEntity> result = repo.findAllBySourceIdAndCondition(source.getId(), condition);
		System.out.println("result = " + result);

		// then
		assertThat(result).isNotEmpty();
		assertThat(result).extracting(FriendEntity::getId).containsOnly(friendEntity.getId());
	}

}