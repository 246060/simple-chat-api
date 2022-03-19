package xyz.jocn.chat;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.room.RoomEntity;
import xyz.jocn.chat.user.UserEntity;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class JpaTest {

	@Autowired
	EntityManager em;

	@DisplayName("id 값만 있는 걸로 fk 값이 들어가는지 확인")
	@Test
	void test1() {
		UserEntity user = UserEntity.builder().email("hello1111111@test.com").build();
		em.persist(user);

		Long userId = user.getId();

		RoomEntity r = RoomEntity.builder().user(new UserEntity(userId)).build();
		em.persist(r);
		em.flush();
		em.clear();
	}

}