package xyz.jocn.chat.participant.repo.room_participant;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.room.RoomEntity;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.user.UserEntity;

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

	}

	@Test
	void findAllByUserId() {
		// given
		UserEntity user = UserEntity.builder().email("hello@g.com").build();
		em.persist(user);
		em.flush();

		System.out.println("user = " + user);

		for (int i = 0; i < 3; i++) {
			RoomEntity room = RoomEntity.builder().user(user).build();
			em.persist(room);

			RoomParticipantEntity participant = RoomParticipantEntity.builder().room(room).user(user).build();
			em.persist(participant);
		}
		em.flush();
		em.clear();

		// when
		List<RoomParticipantEntity> all = repo.findAllByUserId(user.getId());
		System.out.println("all = " + all);

		// then
		assertThat(all).isNotEmpty();
		assertThat(all)
			.extracting(RoomParticipantEntity::getUser)
			.allMatch(userEntity -> userEntity.getId() == user.getId());
	}

	@Test
	void findAllByRoomId() {
		// given
		UserEntity user = UserEntity.builder().email("hello@g.com").build();
		em.persist(user);
		System.out.println("user = " + user);

		Long roomId = 2L;
		for (int i = 0; i < 3; i++) {
			RoomEntity room = RoomEntity.builder().user(user).build();
			em.persist(room);
			roomId = room.getId();

			RoomParticipantEntity participant = RoomParticipantEntity.builder().room(room).user(user).build();
			em.persist(participant);
		}
		em.flush();

		// when
		List<RoomParticipantEntity> all = repo.findAllByRoomId(roomId);
		System.out.println("all = " + all);

		// then
		Long finalRoomId = roomId;
		assertThat(all).isNotEmpty();
		assertThat(all)
			.extracting(RoomParticipantEntity::getRoom)
			.allMatch(roomEntity -> roomEntity.getId() == finalRoomId);
	}

	@Test
	void findByRoomIdAndUserId() {
		// given
		UserEntity user = UserEntity.builder().email("hello@g.com").build();
		em.persist(user);
		em.flush();
		Long userId = user.getId();

		System.out.println("user = " + user);

		Long roomId = 2L;

		for (int i = 0; i < 3; i++) {
			RoomEntity room = RoomEntity.builder().user(user).build();
			em.persist(room);
			roomId = room.getId();

			RoomParticipantEntity participant = RoomParticipantEntity.builder().room(room).user(user).build();
			em.persist(participant);
		}
		em.flush();
		em.clear();

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