package xyz.jocn.chat.message.service;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.common.pubsub.EventTarget.*;
import static xyz.jocn.chat.common.pubsub.EventType.*;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.NotAvailableFeatureException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.common.pubsub.EventDto;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.message.converter.ThreadMessageConverter;
import xyz.jocn.chat.message.dto.ThreadMessageDto;
import xyz.jocn.chat.message.dto.ThreadMessageReactionAddRequestDto;
import xyz.jocn.chat.message.dto.ThreadMessageReactionDto;
import xyz.jocn.chat.message.dto.ThreadMessageSendRequestDto;
import xyz.jocn.chat.message.entity.ThreadMessageEntity;
import xyz.jocn.chat.message.entity.ThreadMessageReactionEntity;
import xyz.jocn.chat.message.enums.ThreadMessageState;
import xyz.jocn.chat.message.repo.thread_message.ThreadMessageRepository;
import xyz.jocn.chat.message.repo.thread_message_reaction.ThreadMessageReactionRepository;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;
import xyz.jocn.chat.thread.ThreadEntity;
import xyz.jocn.chat.thread.repo.ThreadRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ThreadMessageService {

	private final RoomParticipantRepository roomParticipantRepository;

	private final ThreadRepository threadRepository;
	private final ThreadParticipantRepository threadParticipantRepository;
	private final ThreadMessageRepository threadMessageRepository;
	private final ThreadMessageReactionRepository threadMessageReactionRepository;

	private final ThreadMessageConverter threadMessageConverter = ThreadMessageConverter.INSTANCE;

	private final MessagePublisher publisher;

	@Value("${app.publish-event-trigger}")
	public boolean isPublishEventTrigger;

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public void sendThreadMessage(long parseLong, Long threadId, ThreadMessageSendRequestDto dto) {

		switch (dto.getType()) {
			case SHORT_TEXT:
				sendSimpleMessageToThread(dto);
				break;
			case LONG_TEXT:
				break;
			case FILE:
				break;
			default:
				throw new NotAvailableFeatureException();
		}

		if (isPublishEventTrigger) {
			publisher.emit(
				EventDto.builder()
					.target(THREAD_AREA)
					.type(THREAD_MESSAGE_EVENT)
					.spaceId(threadId)
					.build()
			);
		}

	}

	private void sendSimpleMessageToThread(ThreadMessageSendRequestDto dto) {

		ThreadParticipantEntity threadParticipantEntity = null;
		// threadParticipantRepository
		// .findByThreadIdAAndUserId(dto.getThreadId(), dto.getUserId())
		// .orElseThrow(() -> new ResourceNotFoundException(THREAD_PARTICIPANT));

		threadMessageRepository.save(
			ThreadMessageEntity.builder()
				.thread(threadParticipantEntity.getThread())
				.threadParticipant(threadParticipantEntity)
				.message(dto.getMessage())
				.build()
		);
	}

	@Transactional
	public void deleteThreadMessage(long uid, long threadId, long messageId) {

		ThreadMessageEntity threadMessageEntity = threadMessageRepository
			.findById(messageId)
			.orElseThrow(() -> new ResourceNotFoundException(THREAD_MESSAGE));

		threadMessageEntity.changeState(ThreadMessageState.DELETED);

		if (isPublishEventTrigger) {
			publisher.emit(
				EventDto.builder()
					.target(THREAD_AREA)
					.type(THREAD_MESSAGE_EVENT)
					.spaceId(threadMessageEntity.getThread().getId())
					.build()
			);
		}
	}

	@Transactional
	public void addThreadMessageReaction(
		Long uid, Long threadId, Long messageId,
		ThreadMessageReactionAddRequestDto dto
	) {
		ThreadMessageEntity threadMessageEntity = threadMessageRepository
			.findById(messageId)
			.orElseThrow(() -> new ResourceNotFoundException(THREAD_MESSAGE));

		ThreadEntity threadEntity = threadRepository
			.findById(threadId)
			.orElseThrow(() -> new ResourceNotFoundException(THREAD));

		RoomParticipantEntity roomParticipantEntity = roomParticipantRepository
			.findByRoomIdAndUserId(threadEntity.getRoom().getId(), uid)
			.orElseThrow(() -> new ResourceNotFoundException(ROOM_PARTICIPANT));

		threadMessageReactionRepository.save(
			ThreadMessageReactionEntity.builder()
				.type(dto.getType())
				.threadMessage(threadMessageEntity)
				.roomParticipant(roomParticipantEntity)
				.build()
		);
	}

	@Transactional
	public void cancelThreadMessageReaction(Long threadId, Long messageId, Long reactionId) {

		ThreadMessageReactionEntity threadMessageReactionEntity = threadMessageReactionRepository
			.findById(reactionId)
			.orElseThrow(() -> new ResourceNotFoundException(THREAD_MESSAGE_REACTION));

		threadMessageReactionRepository.delete(threadMessageReactionEntity);

		if (isPublishEventTrigger) {
			publisher.emit(
				EventDto.builder()
					.target(THREAD_AREA)
					.type(THREAD_MESSAGE_EVENT)
					.spaceId(threadId)
					.build()
			);
		}
	}

	/*
	 * Query ===============================================================================
	 * */

	public List<ThreadMessageDto> fetchMessages(Long threadId) {
		ThreadEntity threadEntity = threadRepository
			.findById(threadId)
			.orElseThrow(() -> new ResourceNotFoundException(THREAD));

		return threadMessageConverter.toDto(
			threadMessageRepository.findAllByThread(threadEntity)
		);
	}

	public List<ThreadMessageReactionDto> fetchThreadMessageReactions(Long threadId, Long messageId) {
		return Collections.emptyList();
	}

}
