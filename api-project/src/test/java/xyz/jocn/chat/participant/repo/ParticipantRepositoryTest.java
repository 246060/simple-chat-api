package xyz.jocn.chat.participant.repo;

import static org.assertj.core.api.Assertions.*;
import static xyz.jocn.chat.participant.ParticipantState.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.channel.ChannelEntity;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.ParticipantState;
import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.enums.UserState;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ParticipantRepositoryTest {

	@Autowired
	EntityManager em;

	@Autowired
	ParticipantRepository repo;

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
			ChannelEntity channel = ChannelEntity.builder().build();
			em.persist(channel);

			ParticipantEntity participant = ParticipantEntity.builder().channel(channel).user(user).build();
			em.persist(participant);
		}
		em.flush();
		em.clear();

		// when
		List<ParticipantEntity> all = repo.findAllByUserId(user.getId());
		System.out.println("all = " + all);

		// then
		assertThat(all).isNotEmpty();
		assertThat(all)
			.extracting(ParticipantEntity::getUser)
			.allMatch(userEntity -> userEntity.getId() == user.getId());
	}

	@Test
	void findByIdAndChannelId() {
		// given
		UserEntity user = UserEntity.builder().email("hello@g.com").build();
		em.persist(user);
		em.flush();
		Long userId = user.getId();

		System.out.println("user = " + user);

		Long channelId = 2L;

		for (int i = 0; i < 3; i++) {
			ChannelEntity channel = ChannelEntity.builder().build();
			em.persist(channel);
			channelId = channel.getId();

			ParticipantEntity participant = ParticipantEntity.builder().channel(channel).user(user).build();
			em.persist(participant);
		}
		em.flush();
		em.clear();

		// when
		ParticipantEntity entity = repo.findByChannelIdAndUserIdAndState(channelId, userId, JOIN).get();

		// then
		assertThat(entity)
			.extracting(ParticipantEntity::getChannel)
			.extracting(ChannelEntity::getId).isEqualTo(channelId);

		assertThat(entity)
			.extracting(ParticipantEntity::getUser)
			.extracting(UserEntity::getId).isEqualTo(userId);
	}

	@Test
	void findByChannelIdAndUserIdAndState() {
		// given
		UserEntity user1 = UserEntity.builder().email("hello1@g.com").build();
		ChannelEntity channel = ChannelEntity.builder().build();
		ParticipantEntity participant = ParticipantEntity.builder().channel(channel).user(user1).build();

		em.persist(user1);
		em.persist(channel);
		em.persist(participant);
		em.flush();
		em.clear();

		long participantId = participant.getId();
		long channelId = channel.getId();
		long userId = user1.getId();
		ParticipantState state = JOIN;

		// when
		ParticipantEntity result = repo.findByChannelIdAndUserIdAndState(channelId, userId, state).get();
		System.out.println("result = " + result);

		// then
		assertThat(result).extracting(ParticipantEntity::getId).isEqualTo(participantId);
		assertThat(result)
			.extracting(ParticipantEntity::getChannel)
			.extracting(ChannelEntity::getId)
			.isEqualTo(channelId);
		assertThat(result).extracting(ParticipantEntity::getUser).extracting(UserEntity::getId).isEqualTo(userId);
	}

}