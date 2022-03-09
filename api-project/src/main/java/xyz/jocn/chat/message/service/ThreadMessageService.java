package xyz.jocn.chat.message.service;

import static xyz.jocn.chat.common.enums.ResourceType.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.chat_space.entity.ThreadEntity;
import xyz.jocn.chat.chat_space.repo.thread.ThreadRepository;
import xyz.jocn.chat.common.exception.NotAvailableFeatureException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
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
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ThreadMessageService {

	private final ThreadRepository threadRepository;
	private final ThreadParticipantRepository threadParticipantRepository;
	private final ThreadMessageRepository threadMessageRepository;
	private final ThreadMessageMarkRepository threadMessageMarkRepository;

	private final RoomParticipantRepository roomParticipantRepository;

	private final ThreadMessageConverter threadMessageConverter = ThreadMessageConverter.INSTANCE;
	private final ThreadMessageMarkConverter threadMessageMarkConverter = ThreadMessageMarkConverter.INSTANCE;

	@Transactional
	public void send(ThreadMessageCreateDto dto) {
		switch (dto.getType()) {
			case SIMPLE:
				sendSimpleMessageToThread(dto);
				break;
			default:
				throw new NotAvailableFeatureException();
		}
		// ProducerEvent producerEvent = new ProducerEvent();
		// producer.emit(producerEvent);
	}

	private void sendSimpleMessageToThread(ThreadMessageCreateDto dto) {

		ThreadParticipantEntity threadParticipantEntity =
			threadParticipantRepository
				.findByThreadIdAAndUserId(dto.getThreadId(), dto.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException(THREAD_PARTICIPANT));

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
				.findById(dto.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException(THREAD_PARTICIPANT));

		threadMessageMarkRepository.save(
			ThreadMessageMarkEntity.builder()
				.flag(dto.getType())
				.threadMessage(threadMessageEntity)
				.threadParticipant(threadParticipantEntity)
				.build()
		);

		// ProducerEvent producerEvent = new ProducerEvent();
		// producer.emit(producerEvent);
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
		threadMessageMarkRepository.deleteById(markId);
	}

	@Transactional
	public void change(ThreadMessageChangeDto dto) {
		ThreadMessageEntity threadMessageEntity =
			threadMessageRepository
				.findById(dto.getThreadMessageId())
				.orElseThrow(() -> new ResourceNotFoundException(THREAD_MESSAGE));

		threadMessageEntity.changeState(dto.getState());
	}
}
