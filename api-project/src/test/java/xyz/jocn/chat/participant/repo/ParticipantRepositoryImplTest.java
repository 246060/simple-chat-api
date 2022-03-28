package xyz.jocn.chat.participant.repo;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.channel.ChannelEntity;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.dto.ParticipantDto;
import xyz.jocn.chat.user.UserEntity;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ParticipantRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	ParticipantRepositoryImpl repo;

	@BeforeEach
	void setUp() {
	}

	@Test
	void findAllCurrentParticipants() {
		// given
		UserEntity user = UserEntity.builder().name("user01").build();
		em.persist(user);
		System.out.println("user = " + user);

		ChannelEntity channel = ChannelEntity.builder().build();
		ParticipantEntity participant = ParticipantEntity.builder().user(user).build();
		participant.join(channel);
		System.out.println("participant = " + participant);

		em.persist(channel);
		em.persist(participant);
		em.flush();
		em.clear();

		// when
		List<ParticipantDto> participantDtos = repo.findCurrentParticipantsInChannel(channel.getId());
		participantDtos.forEach(System.out::println);

		// then
		assertThat(participantDtos).isNotEmpty();
		assertThat(participantDtos).extracting(ParticipantDto::getId).contains(participant.getId());
		assertThat(participantDtos)
			.extracting(ParticipantDto::getUser)
			.allMatch(userDto -> userDto.getId() == user.getId());
	}
}