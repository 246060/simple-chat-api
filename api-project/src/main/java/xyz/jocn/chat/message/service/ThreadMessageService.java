package xyz.jocn.chat.message.service;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.common.pubsub.EventTarget.*;
import static xyz.jocn.chat.common.pubsub.EventType.*;

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
import xyz.jocn.chat.message.converter.ThreadMessageMarkConverter;
import xyz.jocn.chat.message.dto.ThreadMessageChangeDto;
import xyz.jocn.chat.message.dto.ThreadMessageCreateDto;
import xyz.jocn.chat.message.dto.ThreadMessageDto;
import xyz.jocn.chat.message.dto.ThreadMessageMarkCreateDto;
import xyz.jocn.chat.message.dto.ThreadMessageMarkDto;
import xyz.jocn.chat.message.entity.ThreadMessageEntity;
import xyz.jocn.chat.message.entity.ThreadMessageMarkEntity;
import xyz.jocn.chat.message.repo.thread_message.ThreadMessageRepository;
import xyz.jocn.chat.message.repo.thread_message_mark.ThreadMessageMarkRepository;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;
import xyz.jocn.chat.thread.ThreadEntity;
import xyz.jocn.chat.thread.repo.ThreadRepository;
import xyz.jocn.chat.user.UserEntity;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ThreadMessageService {

	private final ThreadRepository threadRepository;
	private final ThreadParticipantRepository threadParticipantRepository;
	private final ThreadMessageRepository threadMessageRepository;
	private final ThreadMessageMarkRepository threadMessageMarkRepository;

	private final ThreadMessageConverter threadMessageConverter = ThreadMessageConverter.INSTANCE;
	private final ThreadMessageMarkConverter threadMessageMarkConverter = ThreadMessageMarkConverter.INSTANCE;

	private final MessagePublisher publisher;

	@Value("${app.publish-event-trigger}")
	public boolean isPublishEventTrigger;

	@Transactional
	public void send(ThreadMessageCreateDto dto) {

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
					.spaceId(dto.getThreadId())
					.build()
			);
		}
	}

	private void sendSimpleMessageToThread(ThreadMessageCreateDto dto) {

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

	public List<ThreadMessageDto> getMessages(Long threadId) {

		ThreadEntity threadEntity =
			threadRepository
				.findById(threadId)
				.orElseThrow(() -> new ResourceNotFoundException(THREAD));

		List<ThreadMessageEntity> threadMessageEntities =
			threadMessageRepository.findByThread(threadEntity);

		return threadMessageConverter.toDto(threadMessageEntities);
	}

	@Transactional
	public void mark(ThreadMessageMarkCreateDto dto) {

		ThreadMessageEntity threadMessageEntity =
			threadMessageRepository
				.findById(dto.getThreadMessageId())
				.orElseThrow(() -> new ResourceNotFoundException(THREAD_MESSAGE));

		ThreadParticipantEntity threadParticipantEntity =
			threadParticipantRepository
				.findByThreadAndUser(
					threadMessageEntity.getThread(),
					UserEntity.builder().id(dto.getUserId()).build()
				).orElseThrow(() -> new ResourceNotFoundException(THREAD_PARTICIPANT));

		threadMessageMarkRepository.save(
			ThreadMessageMarkEntity.builder()
				.flag(dto.getType())
				.threadMessage(threadMessageEntity)
				.threadParticipant(threadParticipantEntity)
				.build()
		);

		if (isPublishEventTrigger) {
			publisher.emit(
				EventDto.builder()
					.target(THREAD_AREA)
					.type(THREAD_MESSAGE_EVENT)
					.spaceId(threadParticipantEntity.getThread().getId())
					.build()
			);
		}
	}

	public List<ThreadMessageMarkDto> getMarks(Long messageId) {

		ThreadMessageEntity threadMessageEntity =
			threadMessageRepository
				.findById(messageId)
				.orElseThrow(() -> new ResourceNotFoundException(THREAD_MESSAGE));

		return threadMessageMarkConverter.toDto(
			threadMessageMarkRepository.findAllByThreadMessage(threadMessageEntity)
		);
	}

	@Transactional
	public void cancelThreadMessageMark(Long markId) {

		ThreadMessageMarkEntity threadMessageMarkEntity =
			threadMessageMarkRepository
				.findById(markId)
				.orElseThrow(() -> new ResourceNotFoundException(THREAD_MESSAGE_MARK));

		threadMessageMarkRepository.delete(threadMessageMarkEntity);

		if (isPublishEventTrigger) {
			publisher.emit(
				EventDto.builder()
					.target(THREAD_AREA)
					.type(THREAD_MESSAGE_EVENT)
					.spaceId(threadMessageMarkEntity.getThreadParticipant().getThread().getId())
					.build()
			);
		}

	}

	@Transactional
	public void change(ThreadMessageChangeDto dto) {

		ThreadMessageEntity threadMessageEntity =
			threadMessageRepository
				.findById(dto.getThreadMessageId())
				.orElseThrow(() -> new ResourceNotFoundException(THREAD_MESSAGE));

		threadMessageEntity.changeState(dto.getState());

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
}
