package xyz.jocn.chat.friend.repo;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.friend.FriendEntity;
import xyz.jocn.chat.user.entity.UserEntity;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class FriendRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	FriendRepository repo;

	@BeforeEach
	void setUp() {
	}

	@Test
	void findByIdAndSourceId() {
		// given
		UserEntity source = UserEntity.builder().name("user00").email("user00@jocn.xyz").build();
		UserEntity target = UserEntity.builder().name("user01").email("user01@jocn.xyz").build();
		em.persist(source);
		em.persist(target);

		FriendEntity friendEntity = FriendEntity.builder().source(source).target(target).build();
		em.persist(friendEntity);

		em.flush();
		em.clear();

		// when
		FriendEntity result = repo.findByIdAndSourceId(friendEntity.getId(), source.getId()).get();
		System.out.println("result = " + result);

		// then
		assertThat(result).extracting(FriendEntity::getId).isEqualTo(friendEntity.getId());
		assertThat(result).extracting(FriendEntity::getSource).extracting(UserEntity::getId).isEqualTo(source.getId());
	}

	@Test
	void deleteAllBySourceId() {
		// given
		UserEntity source = UserEntity.builder().name("user00").email("user00@jocn.xyz").build();
		UserEntity target = UserEntity.builder().name("user01").email("user01@jocn.xyz").build();
		em.persist(source);
		em.persist(target);

		FriendEntity friendEntity = FriendEntity.builder().source(source).target(target).build();
		em.persist(friendEntity);

		em.flush();
		em.clear();

		long sourceId = friendEntity.getSource().getId();

		List<FriendEntity> readyList =
			em.createQuery("SELECT f FROM FriendEntity f where f.source.id = :sourceId", FriendEntity.class)
				.setParameter("sourceId", sourceId)
				.getResultList();

		int count = readyList.size();
		System.out.println("count = " + count);

		// when
		repo.deleteAllBySourceId(source.getId());

		List<FriendEntity> resultList =
			em.createQuery("SELECT f FROM FriendEntity f where f.source.id = :sourceId", FriendEntity.class)
				.setParameter("sourceId", sourceId)
				.getResultList();

		int result = resultList.size();
		System.out.println("result = " + result);

		// then
		assertThat(result).isNotEqualTo(count);
	}
}