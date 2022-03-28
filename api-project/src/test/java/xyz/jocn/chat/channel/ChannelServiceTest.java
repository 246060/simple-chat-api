package xyz.jocn.chat.channel;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.channel.dto.ChannelDto;
import xyz.jocn.chat.channel.dto.ChannelOpenRequestDto;
import xyz.jocn.chat.channel.repo.ChannelRepository;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.repo.ParticipantRepository;
import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.repo.UserRepository;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

	@Mock
	ChannelRepository channelRepository;
	@Mock
	UserRepository userRepository;
	@Mock
	ParticipantRepository participantRepository;
	@Mock
	MessagePublisher publisher;
	@InjectMocks
	ChannelService channelService;

	@Test
	void open() {
		// given
		Long hostID = 1L;
		Long inviteeId = 2L;
		UserEntity invitee = UserEntity.builder().id(inviteeId).build();
		ChannelEntity room = ChannelEntity.builder().id(1L).build();

		given(userRepository.findById(anyLong())).willReturn(Optional.of(invitee));

		given(channelRepository.save(any(ChannelEntity.class))).willReturn(room);
		given(participantRepository.save(any(ParticipantEntity.class))).willReturn(null);

		// willDoNothing().given(publisher).emit(any(EventDto.class));

		ChannelOpenRequestDto dto = new ChannelOpenRequestDto();
		dto.setInviteeId(inviteeId);

		// when
		Long id = channelService.open(hostID, inviteeId);

		// then
		then(userRepository).should().findById(anyLong());
		then(channelRepository).should().save(any(ChannelEntity.class));
		then(participantRepository).should(times(2)).save(any(ParticipantEntity.class));

		assertThat(id).isNotNull();
		assertThat(id).isEqualTo(room.getId());
	}

	@Test
	void getRoomList() {
		// given
		Long userId = 1L;
		List<ParticipantEntity> entities = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			entities.add(ParticipantEntity.builder()
				.id(1L + i)
				.channel(ChannelEntity.builder().id(1L + i).build())
				.build());
		}
		given(participantRepository.findAllByUserId(anyLong())).willReturn(entities);

		// when
		List<ChannelDto> result = channelService.fetchMyChannels(userId);

		// then
		then(participantRepository).should().findAllByUserId(anyLong());

		assertThat(result).isNotEmpty();
		assertThat(result).extracting(ChannelDto::getId)
			.containsAll(entities.stream()
				.map(ParticipantEntity::getChannel)
				.map(ChannelEntity::getId)
				.collect(Collectors.toList())
			);

	}
}