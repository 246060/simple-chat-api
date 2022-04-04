package xyz.jocn.chat.participant;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static xyz.jocn.chat.participant.ParticipantState.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.FakeUser;
import xyz.jocn.chat.channel.ChannelEntity;
import xyz.jocn.chat.channel.repo.ChannelRepository;
import xyz.jocn.chat.message.repo.MessageRepository;
import xyz.jocn.chat.notification.ChatPushService;
import xyz.jocn.chat.participant.dto.ChannelExitDto;
import xyz.jocn.chat.participant.dto.ChannelInviteRequestDto;
import xyz.jocn.chat.participant.dto.ParticipantDto;
import xyz.jocn.chat.participant.repo.ParticipantRepository;
import xyz.jocn.chat.user.dto.UserDto;
import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.repo.UserRepository;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

	@Mock
	UserRepository userRepository;
	@Mock
	ChannelRepository channelRepository;
	@Mock
	ParticipantRepository participantRepository;
	@Mock
	MessageRepository messageRepository;
	@Mock
	ChatPushService chatPushService;

	@InjectMocks
	ParticipantService service;

	@Test
	void invite() {
		// given
		long uid = 1L;
		long channelId = 1L;
		List<Long> invitees = List.of(2L);

		ChannelInviteRequestDto dto = new ChannelInviteRequestDto();
		dto.setInvitees(invitees);

		ParticipantEntity participantEntity = ParticipantEntity.builder().build();
		given(participantRepository.findByChannelIdAndUserIdAndState(channelId, uid, JOIN))
			.willReturn(Optional.of(participantEntity));

		UserEntity userEntity = UserEntity.builder().id(invitees.get(0)).build();
		given(userRepository.findById(invitees.get(0))).willReturn(Optional.of(userEntity));

		given(
			participantRepository.findByChannelIdAndUserIdAndState(channelId, invitees.get(0), JOIN)
		).willReturn(Optional.empty());

		ChannelEntity channelEntity = ChannelEntity.builder().build();
		given(channelRepository.findById(channelId)).willReturn(Optional.of(channelEntity));
		given(messageRepository.findLastMessageIdInChannel(channelId)).willReturn(Optional.ofNullable(null));
		given(participantRepository.saveAll(any(List.class))).willReturn(Collections.emptyList());

		// when
		service.invite(uid, channelId, dto);

		// then
		then(participantRepository).should(times(1))
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN);
		then(userRepository).should().findById(invitees.get(0));
		then(participantRepository).should(times(1))
			.findByChannelIdAndUserIdAndState(channelId, invitees.get(0), JOIN);

		then(channelRepository).should().findById(channelId);
		then(messageRepository).should().findLastMessageIdInChannel(channelId);
		then(participantRepository).should().saveAll(any(List.class));
	}

	@Test
	void exit() {
		// given
		ChannelExitDto dto = new ChannelExitDto();
		dto.setChannelId(1L);
		dto.setParticipantId(1L);
		dto.setUserId(1L);

		UserEntity user = FakeUser.generateUser("user00");
		ParticipantEntity participantEntity = ParticipantEntity.builder()
			.user(user)
			.build();

		given(participantRepository.findByChannelIdAndUserIdAndState(dto.getChannelId(), dto.getUserId(), JOIN))
			.willReturn(Optional.of(participantEntity));

		// when
		service.exit(dto);

		// then
		then(participantRepository)
			.should(times(1))
			.findByChannelIdAndUserIdAndState(dto.getChannelId(), dto.getUserId(), JOIN);
	}

	@Test
	void fetchCurrentParticipantsInChannel() {
		// given
		long uid = 1L;
		long channelId = 1L;
		long participantId = 1L;

		given(participantRepository.findByChannelIdAndUserIdAndState(channelId, uid, JOIN))
			.willReturn(Optional.of(ParticipantEntity.builder().build()));

		ParticipantDto participantDto = new ParticipantDto();
		participantDto.setId(participantId);

		UserDto userDto = new UserDto();
		userDto.setId(uid);
		userDto.setEmail("user01@test.org");
		userDto.setName("user01");

		participantDto.setUser(userDto);

		List<ParticipantDto> participantDtos = new ArrayList<>();
		participantDtos.add(participantDto);

		given(participantRepository.findParticipantsInChannel(channelId))
			.willReturn(participantDtos);

		// when
		List<ParticipantDto> result = service.fetchCurrentParticipantsInChannel(uid, channelId);
		System.out.println("result = " + result);

		// then
		assertThat(result).isNotEmpty();
		assertThat(result).extracting(ParticipantDto::getId).contains(participantId);
		assertThat(result).extracting(ParticipantDto::getUser).extracting(UserDto::getId).contains(uid);

		then(participantRepository).should().findByChannelIdAndUserIdAndState(channelId, uid, JOIN);
		then(participantRepository).should().findParticipantsInChannel(channelId);
	}

	@Test
	void fetchParticipant() {
		// given
		long uid = 1L;
		long channelId = 1L;
		long participantId = 1L;

		ParticipantEntity participantEntity = ParticipantEntity.builder().id(participantId).build();

		given(participantRepository.findByChannelIdAndUserIdAndState(channelId, uid, JOIN))
			.willReturn(Optional.of(participantEntity));

		// when
		ParticipantEntity result = service.fetchParticipant(channelId, uid);
		System.out.println("result = " + result);

		// then
		assertThat(result).extracting(ParticipantEntity::getId).isEqualTo(participantEntity.getId());

		then(participantRepository).should().findByChannelIdAndUserIdAndState(channelId, uid, JOIN);
	}

	@Test
	void fetchOneInChannel() {
		// given
		long uid = 1L;
		long channelId = 1L;
		long participantId = 1L;

		given(participantRepository.findByChannelIdAndUserIdAndState(channelId, uid, JOIN))
			.willReturn(Optional.of(ParticipantEntity.builder().build()));

		UserEntity userEntity = UserEntity.builder().id(uid).build();
		ParticipantEntity participantEntity = ParticipantEntity.builder().id(participantId).user(userEntity).build();
		given(participantRepository.findById(participantId)).willReturn(Optional.of(participantEntity));

		// when
		ParticipantDto result = service.fetchOneInChannel(uid, channelId, participantId);
		System.out.println("result = " + result);

		// then
		assertThat(result).isNotNull();
		assertThat(result).extracting(ParticipantDto::getId).isEqualTo(participantId);

		then(participantRepository).should(times(1)).findByChannelIdAndUserIdAndState(channelId, uid, JOIN);
		then(participantRepository).should(times(1)).findById(participantId);
	}
}