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
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.ParticipantState;
import xyz.jocn.chat.participant.dto.ParticipantDto;
import xyz.jocn.chat.participant.repo.ParticipantRepository;
import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.dto.UserDto;
import xyz.jocn.chat.user.repo.UserRepository;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {
	@Mock
	ChannelRepository channelRepository;
	@Mock
	UserRepository userRepository;
	@Mock
	ParticipantRepository participantRepository;
	@InjectMocks
	ChannelService channelService;

	@Test
	void open() {
		// given
		Long hostID = 1L;
		Long inviteeId = 2L;
		Long channelId = 1L;

		UserEntity host = UserEntity.builder().id(hostID).build();
		UserEntity invitee = UserEntity.builder().id(inviteeId).build();
		given(userRepository.findById(hostID)).willReturn(Optional.of(host));
		given(userRepository.findById(inviteeId)).willReturn(Optional.of(invitee));

		ChannelEntity channelEntity = ChannelEntity.builder().id(channelId).build();
		System.out.println("channelEntity = " + channelEntity);
		given(channelRepository.save(any(ChannelEntity.class))).willReturn(channelEntity);

		// willDoNothing().given(publisher).emit(any(EventDto.class));

		// when
		Long result = channelService.open(hostID, inviteeId);
		System.out.println("result = " + result);

		// then
		assertThat(result).isNotNull();
		assertThat(result).isGreaterThan(0L);

		then(userRepository).should(times(2)).findById(anyLong());
		then(channelRepository).should().save(any(ChannelEntity.class));
	}

	@Test
	void fetchMyChannels() {
		// given
		long channelId = 1L;
		long participantId = 1L;
		long uid = 1L;

		List<ParticipantDto> participantDtos = new ArrayList<>();

		UserDto userDto = new UserDto();
		userDto.setId(uid);
		userDto.setEmail(String.format("user%d@test.org", uid));
		userDto.setName(String.format("user%d", uid));

		ParticipantDto participantDto = new ParticipantDto();
		participantDto.setId(participantId);
		participantDto.setUser(userDto);

		participantDtos.add(participantDto);

		for (int i = 10; i < 13; i++) {
			long userId = (i + 1);
			UserDto user = new UserDto();
			user.setId(userId);
			user.setEmail(String.format("user%d@test.org", userId));
			user.setName(String.format("user%d", userId));

			ParticipantDto participant = new ParticipantDto();
			participant.setId(1L + i);
			participant.setUser(user);

			participantDtos.add(participant);
		}

		ChannelDto channelDto = new ChannelDto();
		channelDto.setId(channelId);
		channelDto.setParticipants(participantDtos);

		List<ChannelDto> channelDtos = new ArrayList<>();
		channelDtos.add(channelDto);

		given(channelRepository.findAllMyChannels(uid)).willReturn(channelDtos);

		// when
		List<ChannelDto> result = channelService.fetchMyChannels(uid);
		System.out.println("result = " + result);

		// then
		assertThat(result).isNotEmpty();

		for (ChannelDto channel : result) {
			assertThat(channel).extracting(ChannelDto::getId).isEqualTo(channelId);
			assertThat(channel).extracting(ChannelDto::getParticipants).isNotNull();

			assertThat(channel.getParticipants())
				.extracting(ParticipantDto::getId)
				.contains(participantId);

			assertThat(channel.getParticipants())
				.extracting(ParticipantDto::getUser)
				.extracting(UserDto::getId)
				.contains(uid);
		}

		then(channelRepository).should(times(1)).findAllMyChannels(anyLong());
	}

	@Test
	void fetchMyChannel() {
		// given
		long channelId = 1L;
		long participantId = 1L;
		long uid = 1L;

		List<ParticipantDto> participantDtos = new ArrayList<>();

		UserDto userDto = new UserDto();
		userDto.setId(uid);
		userDto.setEmail(String.format("user%d@test.org", uid));
		userDto.setName(String.format("user%d", uid));

		ParticipantDto participantDto = new ParticipantDto();
		participantDto.setId(participantId);
		participantDto.setUser(userDto);

		participantDtos.add(participantDto);

		for (int i = 10; i < 13; i++) {
			long userId = (i + 1);
			UserDto user = new UserDto();
			user.setId(userId);
			user.setEmail(String.format("user%d@test.org", userId));
			user.setName(String.format("user%d", userId));

			ParticipantDto participant = new ParticipantDto();
			participant.setId(1L + i);
			participant.setUser(user);

			participantDtos.add(participant);
		}

		ChannelDto channelDto = new ChannelDto();
		channelDto.setId(channelId);
		channelDto.setParticipants(participantDtos);

		given(participantRepository.findByChannelIdAndUserIdAndState(anyLong(), anyLong(), any(ParticipantState.class)))
			.willReturn(Optional.of(ParticipantEntity.builder().build()));

		given(channelRepository.findMyChannelById(anyLong())).willReturn(Optional.of(channelDto));

		// when
		ChannelDto result = channelService.fetchMyChannel(uid, channelId);
		System.out.println("result = " + result);

		// then
		assertThat(result).isNotNull();
		assertThat(result).extracting(ChannelDto::getId).isEqualTo(channelId);
		assertThat(result).extracting(ChannelDto::getParticipants).isNotNull();

		assertThat(result.getParticipants())
			.extracting(ParticipantDto::getId)
			.contains(participantId);

		assertThat(result.getParticipants())
			.extracting(ParticipantDto::getUser)
			.extracting(UserDto::getId)
			.contains(uid);

		then(participantRepository).should(times(1))
			.findByChannelIdAndUserIdAndState(anyLong(), anyLong(), any(ParticipantState.class));

		then(channelRepository).should(times(1)).findMyChannelById(anyLong());

	}
}