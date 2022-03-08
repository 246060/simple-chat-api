package xyz.jocn.chat.message.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.message.converter.ThreadMessageConverter;
import xyz.jocn.chat.message.converter.ThreadMessageMarkConverter;
import xyz.jocn.chat.message.dto.ThreadMessageCreateDto;
import xyz.jocn.chat.message.dto.ThreadMessageDto;
import xyz.jocn.chat.message.dto.ThreadMessageMarkCreateDto;
import xyz.jocn.chat.message.dto.ThreadMessageMarkDto;
import xyz.jocn.chat.message.entity.ThreadMessageEntity;
import xyz.jocn.chat.message.entity.ThreadMessageMarkEntity;
import xyz.jocn.chat.message.exception.NotAvailableMessageTypeException;
import xyz.jocn.chat.message.exception.NotFoundThreadMessageException;
import xyz.jocn.chat.message.repo.thread_message_mark.ThreadMessageMarkRepository;
import xyz.jocn.chat.message.repo.thread_message.ThreadMessageRepository;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;
import xyz.jocn.chat.participant.exception.NotFoundThreadParticipantException;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;
import xyz.jocn.chat.chat_space.entity.ThreadEntity;
import xyz.jocn.chat.chat_space.exception.NotFoundThreadException;
import xyz.jocn.chat.chat_space.repo.thread.ThreadRepository;

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

	@Transactional
	public void sendMessageToThread(ThreadMessageCreateDto dto) {
		switch (dto.getType()) {
			case SIMPLE:
				sendSimpleMessageToThread(dto);
			default:
				throw new NotAvailableMessageTypeException();
		}
	}

	private void sendSimpleMessageToThread(ThreadMessageCreateDto dto) {

		ThreadParticipantEntity threadParticipantEntity =
			threadParticipantRepository
				.findById(dto.getThreadParticipantId())
				.orElseThrow(NotFoundThreadParticipantException::new);

		threadMessageRepository.save(
			ThreadMessageEntity.builder()
				.thread(threadParticipantEntity.getThread())
				.threadParticipant(threadParticipantEntity)
				.message(dto.getMessage())
				.build()
		);

		// ProducerEvent producerEvent = new ProducerEvent();
		// producer.emit(producerEvent);
	}

	public List<ThreadMessageDto> getMessagesInThread(Long threadId) {
		ThreadEntity threadEntity =
			threadRepository
				.findById(threadId)
				.orElseThrow(NotFoundThreadException::new);

		return threadMessageConverter.toDto(threadMessageRepository.findByThread(threadEntity));
	}

	@Transactional
	public void putMarkOnThreadMessage(ThreadMessageMarkCreateDto dto) {

		ThreadMessageEntity threadMessageEntity =
			threadMessageRepository
				.findById(dto.getThreadMessageId())
				.orElseThrow(NotFoundThreadMessageException::new);

		ThreadParticipantEntity threadParticipantEntity =
			threadParticipantRepository
				.findById(dto.getThreadParticipantId())
				.orElseThrow(NotFoundThreadParticipantException::new);

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

	public List<ThreadMessageMarkDto> getThreadMessageMarks(Long threadMessageId) {
		ThreadMessageEntity threadMessageEntity =
			threadMessageRepository
				.findById(threadMessageId)
				.orElseThrow(NotFoundThreadMessageException::new);

		return threadMessageMarkConverter.toDto(
			threadMessageMarkRepository.findAllByThreadMessage(threadMessageEntity)
		);
	}

	@Transactional
	public void cancelThreadMessageMark(Long markId) {
		threadMessageMarkRepository.deleteById(markId);
	}
}
