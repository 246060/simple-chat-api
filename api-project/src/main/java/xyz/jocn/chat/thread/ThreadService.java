package xyz.jocn.chat.thread;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.common.pubsub.EventTarget.*;
import static xyz.jocn.chat.common.pubsub.EventType.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.thread.dto.ThreadDto;
import xyz.jocn.chat.thread.dto.ThreadOpenDto;
import xyz.jocn.chat.thread.repo.ThreadRepository;
import xyz.jocn.chat.common.exception.ResourceAlreadyExistException;
import xyz.jocn.chat.common.pubsub.EventDto;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.message.entity.RoomMessageEntity;
import xyz.jocn.chat.message.repo.room_message.RoomMessageRepository;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;
import xyz.jocn.chat.user.UserEntity;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ThreadService {

	private final RoomMessageRepository roomMessageRepository;
	private final ThreadRepository threadRepository;
	private final ThreadParticipantRepository threadParticipantRepository;

	private final ThreadConverter threadConverter = ThreadConverter.INSTANCE;

	private final MessagePublisher publisher;

	@Transactional
	public ThreadDto open(ThreadOpenDto threadOpenDto) {

		RoomMessageEntity roomMessageEntity =
			roomMessageRepository
				.findById(threadOpenDto.getRoomMessageId())
				.orElseThrow(() -> new ResourceAlreadyExistException(ROOM_MESSAGE));

		if (roomMessageEntity.hasThread()) {
			throw new ResourceAlreadyExistException(THREAD);
		}

		roomMessageEntity.openThread();

		ThreadEntity threadEntity =
			threadRepository.save(
				ThreadEntity.builder()
					.roomMessage(roomMessageEntity)
					.room(roomMessageEntity.getRoom())
					.build()
			);

		List<UserEntity> users = new ArrayList<>();
		users.add(new UserEntity(threadOpenDto.getUserId()));
		users.add(roomMessageEntity.getSender().getUser());

		for (UserEntity user : users) {
			threadParticipantRepository.save(
				ThreadParticipantEntity.builder().user(user).thread(threadEntity).build()
			);
		}

		{ // event
			List<Long> receivers = users.stream().map(UserEntity::getId).collect(Collectors.toList());
			publisher.emit(
				EventDto.builder()
					.target(INDIVIDUAL)
					.type(THREAD_EVENT)
					.receiver(Collections.singletonList(receivers))
					.spaceId(threadEntity.getId())
					.build()
			);
		}

		return threadConverter.toDto(threadEntity);
	}

	public List<ThreadDto> getThreads(long userId) {
		return threadParticipantRepository
			.findAllByUserId(userId)
			.stream()
			.map(threadParticipantEntity -> threadConverter.toDto(threadParticipantEntity))
			.collect(Collectors.toList());
	}
}
