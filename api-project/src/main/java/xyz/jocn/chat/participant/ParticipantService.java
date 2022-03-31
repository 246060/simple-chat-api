package xyz.jocn.chat.participant;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.participant.ParticipantState.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.channel.ChannelEntity;
import xyz.jocn.chat.channel.repo.ChannelRepository;
import xyz.jocn.chat.common.exception.ResourceAlreadyExistException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.notification.ChatPushService;
import xyz.jocn.chat.participant.dto.ChannelExitDto;
import xyz.jocn.chat.participant.dto.ChannelInviteRequestDto;
import xyz.jocn.chat.participant.dto.ParticipantDto;
import xyz.jocn.chat.participant.repo.ParticipantRepository;
import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.repo.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ParticipantService {

	private final UserRepository userRepository;
	private final ChannelRepository channelRepository;
	private final ParticipantRepository participantRepository;

	private final ChatPushService chatPushService;

	private ParticipantConverter participantConverter = ParticipantConverter.INSTANCE;

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public void invite(long uid, long channelId, ChannelInviteRequestDto dto) {

		participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		ChannelEntity channel = channelRepository
			.findById(channelId)
			.orElseThrow(() -> new ResourceNotFoundException(CHANNEL));

		List<UserEntity> invitees = dto.getInvitees().stream()
			.map(id -> userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER)))
			.collect(Collectors.toList());

		List<ParticipantEntity> newParticipants = invitees.stream()
			.map(user -> ParticipantEntity.builder().channel(channel).user(user).build())
			.collect(Collectors.toList());

		for (ParticipantEntity newParticipant : newParticipants) {
			participantRepository
				.findByChannelIdAndUserIdAndState(channelId, newParticipant.getUser().getId(), JOIN)
				.ifPresent(participantEntity -> {
					throw new ResourceAlreadyExistException(PARTICIPANT);
				});
		}

		participantRepository.saveAll(newParticipants);
		chatPushService.pushChannelInviteEvent(channelId, invitees);
	}

	@Transactional
	public void exit(ChannelExitDto dto) {

		ParticipantEntity participantEntity = participantRepository
			.findByChannelIdAndUserIdAndState(dto.getChannelId(), dto.getUserId(), JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		participantEntity.exit();
		chatPushService.pushChannelExitEvent(dto.getChannelId(), participantEntity.getUser());
	}

	/*
	 * Query ===============================================================================
	 * */

	public List<ParticipantDto> fetchCurrentParticipantsInChannel(long uid, long channelId) {
		participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		return participantRepository.findCurrentParticipantsInChannel(channelId);
	}

	public ParticipantEntity fetchParticipant(long channelId, long uid) {
		return participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));
	}

	public void checkParticipant(long channelId, long uid) {
		this.fetchParticipant(channelId, uid);
	}

	public ParticipantDto fetchOneInChannel(long uid, long channelId, long participantId) {
		participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		ParticipantEntity participantEntity = participantRepository
			.findById(participantId)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		participantEntity.getUser().getId();
		return participantConverter.toDto(participantEntity);
	}
}
