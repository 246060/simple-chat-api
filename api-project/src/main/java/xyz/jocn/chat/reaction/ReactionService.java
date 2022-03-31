package xyz.jocn.chat.reaction;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.notification.enums.RoutingType.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.notification.dto.EventDto;
import xyz.jocn.chat.notification.dto.EvtRoutingDto;
import xyz.jocn.chat.notification.dto.EvtReactionDto;
import xyz.jocn.chat.message.entity.MessageEntity;
import xyz.jocn.chat.message.repo.MessageRepository;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.ParticipantService;
import xyz.jocn.chat.reaction.dto.ReactionAddRequestDto;
import xyz.jocn.chat.reaction.dto.ReactionDto;
import xyz.jocn.chat.reaction.repo.ReactionRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReactionService {

	private final ReactionRepository reactionRepository;
	private final MessageRepository messageRepository;
	private final ParticipantService participantService;

	private final ReactionConverter reactionConverter = ReactionConverter.INSTANCE;

	private final MessagePublisher publisher;

	@Value("${app.publish-event-trigger}")
	public boolean isPublishEventTrigger;


	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public void addReaction(long uid, long channelId, long messageId, ReactionAddRequestDto dto) {

		ParticipantEntity participantEntity = participantService.fetchParticipant(channelId, uid);

		MessageEntity messageEntity = messageRepository
			.findById(messageId)
			.orElseThrow(() -> new ResourceNotFoundException(MESSAGE));

		reactionRepository.save(
			ReactionEntity.builder()
				.type(dto.getType())
				.message(messageEntity)
				.participant(participantEntity)
				.build()
		);

		if (isPublishEventTrigger) {
			EventDto chatEvent = new EventDto();
			chatEvent.setRouting(new EvtRoutingDto(channel, String.valueOf(channelId)));
			chatEvent.setMessage(new EvtReactionDto(channelId, messageId));
			publisher.emit(chatEvent);
		}
	}

	@Transactional
	public void cancelReaction(long uid, long channelId, long messageId, long reactionId) {

		ParticipantEntity participantEntity = participantService.fetchParticipant(channelId, uid);

		ReactionEntity reactionEntity = reactionRepository
			.findByIdAndParticipant(reactionId, participantEntity)
			.orElseThrow(() -> new ResourceNotFoundException(REACTION));

		reactionRepository.delete(reactionEntity);

		if (isPublishEventTrigger) {
			EventDto chatEvent = new EventDto();
			chatEvent.setRouting(new EvtRoutingDto(channel, String.valueOf(channelId)));
			chatEvent.setMessage(new EvtReactionDto(channelId, messageId));
			publisher.emit(chatEvent);
		}
	}

	/*
	 * Query ===============================================================================
	 * */

	public List<ReactionDto> fetchReactions(long uid, long channelId, long messageId) {

		participantService.checkParticipant(channelId, uid);

		MessageEntity messageEntity = messageRepository
			.findById(messageId)
			.orElseThrow(() -> new ResourceNotFoundException(MESSAGE));

		return reactionConverter.toDto(
			reactionRepository.findAllByMessage(messageEntity)
		);
	}
}
