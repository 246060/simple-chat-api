package xyz.jocn.chat.channel;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.participant.ParticipantState.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.channel.dto.ChannelDto;
import xyz.jocn.chat.channel.repo.ChannelRepository;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.repo.ParticipantRepository;
import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.repo.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChannelService {

	private final UserRepository userRepository;
	private final ChannelRepository channelRepository;
	private final ParticipantRepository participantRepository;

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public Long open(Long hostId, Long inviteeId) {

		UserEntity host = userRepository.findById(hostId).orElseThrow(() -> new ResourceNotFoundException(USER));
		UserEntity invitee = userRepository.findById(inviteeId).orElseThrow(() -> new ResourceNotFoundException(USER));

		ChannelEntity channel = channelRepository.save(ChannelEntity.builder().build());

		ParticipantEntity participant1 = ParticipantEntity.builder().user(host).build();
		ParticipantEntity participant2 = ParticipantEntity.builder().user(invitee).build();

		participant1.join(channel);
		participant2.join(channel);
		channel.increaseNumberOfParticipants();
		channel.increaseNumberOfParticipants();

		participantRepository.save(participant1);
		participantRepository.save(participant2);

		return channel.getId();
	}

	/*
	 * Query ===============================================================================
	 * */

	public List<ChannelDto> fetchMyChannels(long uid) {
		return channelRepository.findAllMyChannels(uid);
	}

	public ChannelDto fetchMyChannel(long uid, long channelId) {
		participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		return channelRepository
			.findMyChannelById(channelId)
			.orElseThrow(() -> new ResourceNotFoundException(CHANNEL));
	}
}
