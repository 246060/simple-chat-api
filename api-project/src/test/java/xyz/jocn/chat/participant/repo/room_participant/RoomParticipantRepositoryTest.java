package xyz.jocn.chat.participant.repo.room_participant;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import xyz.jocn.chat.chat_space.entity.RoomEntity;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.user.entity.UserEntity;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class RoomParticipantRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	RoomParticipantRepository repo;

	@BeforeEach
	void setUp() {
		UserEntity user = UserEntity.builder().email("hello@g.com").build();
		em.persist(user);
		em.flush();

		System.out.println("user = " + user);

		for (int i = 0; i < 3; i++) {
			RoomEntity room = RoomEntity.builder().user(user).build();
			em.persist(room);

			RoomParticipantEntity participant = RoomParticipantEntity.builder().room(room).user(user).build();
			em.persist(participant);
			em.flush();
		}
	}

	@Test
	void findAllByUserId() {
		// given
		Long userId = 1L;

		// when
		List<RoomParticipantEntity> all = repo.findAllByUserId(userId);

		// then
		assertThat(all).isNotEmpty();
		assertThat(all)
			.extracting(RoomParticipantEntity::getUser)
			.allMatch(userEntity -> userEntity.getId() == userId);
	}

	@Test
	void findAllByRoomId() {
		// given
		Long roomId = 2L;

		// when
		List<RoomParticipantEntity> all = repo.findAllByRoomId(roomId);

		// then
		assertThat(all)
			.extracting(RoomParticipantEntity::getRoom)
			.allMatch(roomEntity -> roomEntity.getId() == roomId);
	}

	@Test
	void findByRoomIdAndUserId() {
		// given
		Long userId = 1L;
		Long roomId = 2L;

		// when
		RoomParticipantEntity entity = repo.findByRoomIdAndUserId(roomId, userId).get();

		// then
		assertThat(entity)
			.extracting(RoomParticipantEntity::getRoom)
			.extracting(RoomEntity::getId).isEqualTo(roomId);

		assertThat(entity)
			.extracting(RoomParticipantEntity::getUser)
			.extracting(UserEntity::getId).isEqualTo(userId);
	}
}