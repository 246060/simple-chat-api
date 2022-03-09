package xyz.jocn.chat.chat_space.service;

import static xyz.jocn.chat.common.enums.ResourceType.THREAD;
import static xyz.jocn.chat.common.enums.ResourceType.*;
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
import xyz.jocn.chat.chat_space.converter.ThreadConverter;
import xyz.jocn.chat.chat_space.dto.ThreadDto;
import xyz.jocn.chat.chat_space.dto.ThreadOpenDto;
import xyz.jocn.chat.chat_space.entity.ThreadEntity;
import xyz.jocn.chat.chat_space.repo.thread.ThreadRepository;
import xyz.jocn.chat.common.exception.ResourceAlreadyExistException;
import xyz.jocn.chat.common.pubsub.ChatProducer;
import xyz.jocn.chat.common.pubsub.PublishEvent;
import xyz.jocn.chat.message.entity.RoomMessageEntity;
import xyz.jocn.chat.message.repo.room_message.RoomMessageRepository;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;
import xyz.jocn.chat.participant.repo.thread_participant.ThreadParticipantRepository;
import xyz.jocn.chat.user.entity.UserEntity;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ThreadService {

	private final RoomMessageRepository roomMessageRepository;
	private final ThreadRepository threadRepository;
	private final ThreadParticipantRepository threadParticipantRepository;

	private final ThreadConverter threadConverter = ThreadConverter.INSTANCE;
	private final ChatProducer chatProducer;

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
		users.add(UserEntity.builder().id(threadOpenDto.getUserId()).build());
		users.add(roomMessageEntity.getSender().getUser());

		for (UserEntity user : users) {
			threadParticipantRepository.save(
				ThreadParticipantEntity.builder()
					.user(user)
					.thread(threadEntity)
					.build()
			);
		}

		{ // pub event
			List<Long> receivers = users.stream().map(UserEntity::getId).collect(Collectors.toList());

			PublishEvent publishEvent = new PublishEvent();
			publishEvent.setTarget(INDIVIDUAL);
			publishEvent.setReceiver(Collections.singletonList(receivers));
			publishEvent.setType(THREAD_OPEN);
			publishEvent.setSpaceId(threadEntity.getId());

			chatProducer.emit(publishEvent);
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
