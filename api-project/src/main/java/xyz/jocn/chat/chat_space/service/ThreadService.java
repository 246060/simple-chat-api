package xyz.jocn.chat.chat_space.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.message.entity.RoomMessageEntity;
import xyz.jocn.chat.message.exception.NotFoundRoomMessageException;
import xyz.jocn.chat.message.repo.room_message.RoomMessageRepository;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;
import xyz.jocn.chat.participant.exception.NotFoundRoomParticipantException;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;
import xyz.jocn.chat.chat_space.dto.ThreadOpenRequestDto;
import xyz.jocn.chat.chat_space.entity.ThreadEntity;
import xyz.jocn.chat.chat_space.repo.thread.ThreadRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ThreadService {

	private final RoomParticipantRepository roomParticipantRepository;
	private final RoomMessageRepository roomMessageRepository;
	private final ThreadRepository threadRepository;
	private final ThreadParticipantRepository threadParticipantRepository;

	@Transactional
	public void openThread(ThreadOpenRequestDto dto) {

		RoomParticipantEntity roomParticipantEntity =
			roomParticipantRepository
				.findById(dto.getParticipantId())
				.orElseThrow(NotFoundRoomParticipantException::new);

		RoomMessageEntity roomMessageEntity =
			roomMessageRepository
				.findById(dto.getMessageId())
				.orElseThrow(NotFoundRoomMessageException::new);

		ThreadEntity threadEntity = threadRepository.save(
			ThreadEntity.builder()
				.roomMessage(roomMessageEntity)
				.build()
		);

		threadParticipantRepository.save(ThreadParticipantEntity.builder()
			.roomParticipant(roomMessageEntity.getSender())
			.thread(threadEntity)
			.build());

		threadParticipantRepository.save(ThreadParticipantEntity.builder()
			.roomParticipant(roomParticipantEntity)
			.thread(threadEntity)
			.build());

		// ProducerEvent producerEvent = new ProducerEvent();
		// producer.emit(producerEvent);
	}
}
