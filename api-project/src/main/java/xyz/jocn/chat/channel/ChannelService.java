package xyz.jocn.chat.channel;

import static xyz.jocn.chat.common.exception.ResourceType.*;

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
import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.repo.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChannelService {

	private final UserRepository userRepository;
	private final ChannelRepository channelRepository;
	private final ParticipantRepository participantRepository;

	private final ChannelConverter channelConverter = ChannelConverter.INSTANCE;

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public Long open(Long hostId, Long inviteeId) {

		UserEntity invitee = userRepository.findById(inviteeId).orElseThrow(() -> new ResourceNotFoundException(USER));
		UserEntity host = userRepository.findById(hostId).orElseThrow(() -> new ResourceNotFoundException(USER));

		ChannelEntity channel = ChannelEntity.builder().build();
		channelRepository.save(channel);

		ParticipantEntity participant1 = ParticipantEntity.builder().user(invitee).build();
		ParticipantEntity participant2 = ParticipantEntity.builder().user(host).build();
		participant1.join(channel);
		participant2.join(channel);
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

	public List<ChannelDto> fetchMyChannel(long uid, long channelId) {
		return channelRepository.findMyChannelById(channelId, uid);
	}
}
