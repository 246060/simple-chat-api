package xyz.jocn.chat.channel.repo;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import xyz.jocn.chat.channel.ChannelEntity;
import xyz.jocn.chat.channel.dto.ChannelDto;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.dto.ParticipantDto;
import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.dto.UserDto;

//@Rollback(false)
//@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class ChannelRepositoryImplTest {

	@Autowired
	EntityManager em;

	@Autowired
	ChannelRepositoryImpl channelRepository;

	@BeforeEach
	void setUp() {
	}

	@DisplayName("나의 채널 리스트")
	@Test
	void fetchChannels() {
		// given
		UserEntity user = UserEntity.builder().name("user00").email("user00@jocn.xyz").build();
		em.persist(user);

		ChannelEntity channel = ChannelEntity.builder().build();
		em.persist(channel);
		em.persist(ChannelEntity.builder().build());
		em.persist(ChannelEntity.builder().build());

		ParticipantEntity participant = ParticipantEntity.builder()
			.channel(channel)
			.user(user)
			.build();

		em.persist(participant);
		em.flush();
		em.clear();

		// when
		List<ChannelDto> channelEntities = channelRepository.findAllMyChannels(user.getId());
		channelEntities.forEach(System.out::println);

		// then
		assertThat(channelEntities).isNotEmpty();
		assertThat(channelEntities).hasSize(1);
		assertThat(channelEntities).extracting(ChannelDto::getId).containsOnly(channel.getId());

		for (ChannelDto channelEntity : channelEntities) {
			List<ParticipantDto> participants = channelEntity.getParticipants();
			for (ParticipantDto participantDto : participants) {
				assertThat(participantDto.getUser()).extracting(UserDto::getId).isEqualTo(user.getId());
			}
		}
	}

}