package xyz.jocn.chat.participant;

import static xyz.jocn.chat.common.exception.ResourceType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.channel.ChannelEntity;
import xyz.jocn.chat.channel.repo.ChannelRepository;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
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

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public void invite(Long channelId, ChannelInviteRequestDto dto) {

		ChannelEntity channelEntity = channelRepository
			.findById(channelId)
			.orElseThrow(() -> new ResourceNotFoundException(CHANNEL));

		List<UserEntity> userEntities = new ArrayList<>();
		for (Long invitee : dto.getInvitees()) {
			userEntities.add(userRepository.findById(invitee).orElseThrow(() -> new ResourceNotFoundException(USER)));
		}

		List<ParticipantEntity> newParticipants = userEntities.stream()
			.map(userEntity -> ParticipantEntity.builder().channel(channelEntity).user(userEntity).build())
			.collect(Collectors.toList());

		participantRepository.saveAll(newParticipants);
	}

	@Transactional
	public void exit(ChannelExitDto dto) {

		ParticipantEntity participantEntity = participantRepository
			.findByIdAndUserId(dto.getParticipantId(), dto.getUserId())
			.orElseThrow(() -> new ResourceNotFoundException(CHANNEL_PARTICIPANT));

		participantEntity.exit();
	}

	/*
	 * Query ===============================================================================
	 * */

	public List<ParticipantDto> fetchCurrentParticipantsInChannel(long channelId) {
		return participantRepository.findCurrentParticipantsInChannel(channelId);
	}

	public ParticipantEntity fetchParticipant(long channelId, long uid) {
		return participantRepository
			.findByChannelIdAndUserId(channelId, uid)
			.orElseThrow(() -> new ResourceNotFoundException(CHANNEL_PARTICIPANT));
	}

	public void checkParticipant(long channelId, long uid) {
		this.fetchParticipant(channelId, uid);
	}
}
