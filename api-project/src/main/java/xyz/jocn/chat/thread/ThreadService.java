package xyz.jocn.chat.thread;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.participant.enums.ParticipantState.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.ResourceAlreadyExistException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.message.entity.RoomMessageEntity;
import xyz.jocn.chat.message.repo.room_message.RoomMessageRepository;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;
import xyz.jocn.chat.thread.dto.ThreadDto;
import xyz.jocn.chat.thread.dto.ThreadOpenDto;
import xyz.jocn.chat.thread.repo.ThreadRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ThreadService {

	private final RoomMessageRepository roomMessageRepository;
	private final RoomParticipantRepository roomParticipantRepository;
	private final ThreadRepository threadRepository;
	private final ThreadParticipantRepository threadParticipantRepository;

	private final ThreadConverter threadConverter = ThreadConverter.INSTANCE;

	@Value("${app.publish-event-trigger}")
	public boolean isPublishEventTrigger;

	private final MessagePublisher publisher;

	@Transactional
	public ThreadDto open(ThreadOpenDto threadOpenDto) {

		RoomMessageEntity roomMessage = roomMessageRepository
			.findById(threadOpenDto.getRoomMessageId())
			.orElseThrow(() -> new ResourceAlreadyExistException(ROOM_MESSAGE));

		if (roomMessage.hasThread()) {
			throw new ResourceAlreadyExistException(THREAD);
		}
		roomMessage.openThread();

		ThreadEntity threadEntity = threadRepository.save(
			ThreadEntity.builder().roomMessage(roomMessage).room(roomMessage.getRoom()).build()
		);

		RoomParticipantEntity threadOpener = roomParticipantRepository
			.findByRoomIdAndUserId(roomMessage.getRoom().getId(), threadOpenDto.getUserId())
			.orElseThrow(() -> new ResourceNotFoundException(ROOM_PARTICIPANT));

		RoomParticipantEntity roomMessageSender = roomParticipantRepository
			.findByRoomIdAndUserId(roomMessage.getRoom().getId(), roomMessage.getSender().getUser().getId())
			.orElseThrow(() -> new ResourceNotFoundException(ROOM_PARTICIPANT));

		List<RoomParticipantEntity> roomParticipantEntities = new ArrayList<>();
		roomParticipantEntities.add(threadOpener);
		roomParticipantEntities.add(roomMessageSender);

		for (RoomParticipantEntity roomParticipantEntity : roomParticipantEntities) {
			threadParticipantRepository.save(
				ThreadParticipantEntity.builder()
					.thread(threadEntity)
					.roomParticipant(roomParticipantEntity)
					.build()
			);
		}

		if (isPublishEventTrigger) {
			// List<Long> receivers = users.stream().map(UserEntity::getId).collect(Collectors.toList());
			// publisher.emit(
			// 	EventDto.builder()
			// 		.target(INDIVIDUAL)
			// 		.type(THREAD_EVENT)
			// 		.receiver(Collections.singletonList(receivers))
			// 		.spaceId(threadEntity.getId())
			// 		.build()
			// );
		}

		return threadConverter.toDto(threadEntity);
	}

	public List<ThreadDto> fetchMyThreads(long uid) {

		List<RoomParticipantEntity> entities = roomParticipantRepository.findAllByUserIdAndState(uid, JOIN);

		return threadConverter.toDto(
			threadParticipantRepository
				.findAllByRoomParticipantIn(entities)
				.stream().map(ThreadParticipantEntity::getThread)
				.collect(Collectors.toList())
		);
	}
}
