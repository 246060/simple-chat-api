package xyz.jocn.chat.room.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.dto.PageDto;
import xyz.jocn.chat.common.exception.NotAvailableMessageTypeException;
import xyz.jocn.chat.common.exception.NotFoundRoomException;
import xyz.jocn.chat.common.exception.NotFoundRoomMessageException;
import xyz.jocn.chat.common.exception.NotFoundRoomParticipantException;
import xyz.jocn.chat.common.exception.NotFoundThreadException;
import xyz.jocn.chat.common.exception.NotFoundThreadMessageException;
import xyz.jocn.chat.common.exception.NotFoundThreadParticipantException;
import xyz.jocn.chat.common.exception.NotFoundUserException;
import xyz.jocn.chat.pubsub.ChatProducer;
import xyz.jocn.chat.room.converter.RoomConverter;
import xyz.jocn.chat.room.converter.RoomMessageMarkConverter;
import xyz.jocn.chat.room.converter.RoomParticipantConverter;
import xyz.jocn.chat.room.converter.ThreadMessageConverter;
import xyz.jocn.chat.room.converter.ThreadMessageMarkConverter;
import xyz.jocn.chat.room.dto.RoomCreateRequestDto;
import xyz.jocn.chat.room.dto.RoomDto;
import xyz.jocn.chat.room.dto.RoomInviteRequestDto;
import xyz.jocn.chat.room.dto.RoomMessageDto;
import xyz.jocn.chat.room.dto.RoomMessageGetRequestDto;
import xyz.jocn.chat.room.dto.RoomMessageMarkCreateRequestDto;
import xyz.jocn.chat.room.dto.RoomMessageMarkDto;
import xyz.jocn.chat.room.dto.RoomMessageSendRequestDto;
import xyz.jocn.chat.room.dto.RoomParticipantDto;
import xyz.jocn.chat.room.dto.ThreadMessageCreateRequestDto;
import xyz.jocn.chat.room.dto.ThreadMessageDto;
import xyz.jocn.chat.room.dto.ThreadMessageMarkCreateRequestDto;
import xyz.jocn.chat.room.dto.ThreadMessageMarkDto;
import xyz.jocn.chat.room.dto.ThreadOpenRequestDto;
import xyz.jocn.chat.room.entity.RoomEntity;
import xyz.jocn.chat.room.entity.RoomMessageEntity;
import xyz.jocn.chat.room.entity.RoomMessageMarkEntity;
import xyz.jocn.chat.room.entity.RoomParticipantEntity;
import xyz.jocn.chat.room.entity.ThreadEntity;
import xyz.jocn.chat.room.entity.ThreadMessageEntity;
import xyz.jocn.chat.room.entity.ThreadMessageMarkEntity;
import xyz.jocn.chat.room.entity.ThreadParticipantEntity;
import xyz.jocn.chat.room.repository.RoomMessageMarkRepository;
import xyz.jocn.chat.room.repository.RoomMessageRepository;
import xyz.jocn.chat.room.repository.RoomParticipantRepository;
import xyz.jocn.chat.room.repository.ThreadMessageMarkRepository;
import xyz.jocn.chat.room.repository.ThreadMessageRepository;
import xyz.jocn.chat.room.repository.ThreadParticipantRepository;
import xyz.jocn.chat.room.repository.ThreadRepository;
import xyz.jocn.chat.room.repository.room.RoomRepository;
import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomService {

	private final UserRepository userRepository;

	private final RoomRepository roomRepository;
	private final RoomMessageRepository roomMessageRepository;
	private final RoomParticipantRepository roomParticipantRepository;
	private final RoomMessageMarkRepository roomMessageMarkRepository;

	private final ThreadRepository threadRepository;
	private final ThreadParticipantRepository threadParticipantRepository;
	private final ThreadMessageRepository threadMessageRepository;
	private final ThreadMessageMarkRepository threadMessageMarkRepository;

	private final RoomConverter roomConverter = RoomConverter.INSTANCE;
	private final RoomParticipantConverter roomParticipantConverter = RoomParticipantConverter.INSTANCE;
	private final RoomMessageMarkConverter roomMessageMarkConverter = RoomMessageMarkConverter.INSTANCE;
	private final ThreadMessageConverter threadMessageConverter = ThreadMessageConverter.INSTANCE;
	private final ThreadMessageMarkConverter threadMessageMarkConverter = ThreadMessageMarkConverter.INSTANCE;

	private final ChatProducer producer;

	@Transactional
	public RoomDto open(RoomCreateRequestDto roomCreateRequestDto) {

		RoomEntity roomEntity = roomRepository.save(RoomEntity.builder().build());

		roomParticipantRepository.save(
			RoomParticipantEntity.builder()
				.room(roomEntity)
				.user(userRepository
					.findById(roomCreateRequestDto.getHostId())
					.orElseThrow(NotFoundUserException::new))
				.build()
		);

		roomParticipantRepository.save(RoomParticipantEntity.builder()
			.room(roomEntity)
			.user(userRepository
				.findById(roomCreateRequestDto.getInviteeId())
				.orElseThrow(NotFoundUserException::new))
			.build()
		);

		// ProducerEvent producerEvent = new ProducerEvent();
		// producer.emit(producerEvent);

		return roomConverter.toDto(roomEntity);
	}

	@Transactional
	public void exit(Long participantId) {
		RoomParticipantEntity roomParticipantEntity = roomParticipantRepository
			.findById(participantId)
			.orElseThrow(NotFoundRoomParticipantException::new);

		Long roomId = roomParticipantEntity.getRoom().getId();

		threadParticipantRepository.deleteAllInBatch(
			threadParticipantRepository.findAllByRoomParticipant(roomParticipantEntity)
		);
		roomParticipantRepository.delete(roomParticipantEntity);

		// ProducerEvent producerEvent = new ProducerEvent();
		// producer.emit(producerEvent);
	}

	@Transactional
	public void invite(RoomInviteRequestDto roomInviteRequestDto) {

		RoomEntity roomEntity =
			roomRepository.findById(roomInviteRequestDto.getRoomId()).orElseThrow(NotFoundRoomException::new);

		List<UserEntity> userEntities =
			userRepository.findByIdIn(roomInviteRequestDto.getInvitees());

		List<RoomParticipantEntity> newParticipants = new ArrayList<>();

		for (UserEntity userEntity : userEntities) {
			newParticipants.add(
				RoomParticipantEntity.builder()
					.room(roomEntity)
					.user(userEntity)
					.build()
			);
		}

		roomParticipantRepository.saveAll(newParticipants);

		// ProducerEvent producerEvent = new ProducerEvent();
		// producer.emit(producerEvent);
	}

	public List<RoomDto> getRoomList(String userId) {

		UserEntity userEntity = userRepository
			.findById(Long.parseLong(userId))
			.orElseThrow(NotFoundUserException::new);

		List<RoomParticipantEntity> participantEntities =
			roomParticipantRepository.findAllByUser(userEntity);

		List<RoomDto> list = new ArrayList<>();
		for (RoomParticipantEntity participantEntity : participantEntities) {
			list.add(roomConverter.toDto(participantEntity.getRoom()));
		}

		return list;
	}

	public List<RoomParticipantDto> getParticipants(Long roomId) {

		RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(NotFoundRoomException::new);

		List<RoomParticipantEntity> participants = roomParticipantRepository.findAllByRoom(roomEntity);
		for (RoomParticipantEntity participant : participants) {
			participant.getUser().getName();
		}

		return roomParticipantConverter.toDto(participants);
	}

	@Transactional
	public void sendMessageToRoom(RoomMessageSendRequestDto roomMessageSendRequestDto) {
		switch (roomMessageSendRequestDto.getType()) {
			case SIMPLE:
				sendSimpleMessageToRoom(roomMessageSendRequestDto);
			default:
				throw new NotAvailableMessageTypeException();
		}

		// ProducerEvent producerEvent = new ProducerEvent();
		// producer.emit(producerEvent);
	}

	private void sendSimpleMessageToRoom(RoomMessageSendRequestDto roomMessageSendRequestDto) {
		RoomEntity roomEntity =
			roomRepository
				.findById(roomMessageSendRequestDto.getRoomId())
				.orElseThrow(NotFoundRoomException::new);

		RoomParticipantEntity roomParticipantEntity =
			roomParticipantRepository
				.findById(roomMessageSendRequestDto.getParticipantId())
				.orElseThrow(NotFoundRoomParticipantException::new);

		RoomMessageEntity roomMessageEntity =
			RoomMessageEntity.builder()
				.type(roomMessageSendRequestDto.getType())
				.room(roomEntity)
				.sender(roomParticipantEntity)
				.build();
	}

	public PageDto<RoomMessageDto> getMessagesInRoom(RoomMessageGetRequestDto dto) {

		return null;
	}

	@Transactional
	public void putMarkOnRoomMessage(RoomMessageMarkCreateRequestDto dto) {

		RoomMessageEntity roomMessageEntity =
			roomMessageRepository
				.findById(dto.getMessageId())
				.orElseThrow(NotFoundRoomMessageException::new);

		RoomParticipantEntity roomParticipantEntity =
			roomParticipantRepository
				.findById(dto.getParticipantId())
				.orElseThrow(NotFoundRoomParticipantException::new);

		roomMessageMarkRepository.save(
			RoomMessageMarkEntity.builder()
				.flag(dto.getType())
				.roomMessage(roomMessageEntity)
				.roomParticipant(roomParticipantEntity)
				.build()
		);

		// ProducerEvent producerEvent = new ProducerEvent();
		// producer.emit(producerEvent);
	}

	public List<RoomMessageMarkDto> getMessageMarks(Long messageId) {
		RoomMessageEntity roomMessageEntity = roomMessageRepository.findById(messageId)
			.orElseThrow(NotFoundRoomMessageException::new);

		List<RoomMessageMarkEntity> roomMessageMarkEntities =
			roomMessageMarkRepository.findAllByRoomMessage(roomMessageEntity);

		return roomMessageMarkConverter.toDto(roomMessageMarkEntities);
	}

	@Transactional
	public void cancelRoomMessageMark(Long markId) {
		roomMessageMarkRepository.deleteById(markId);
	}

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

	@Transactional
	public void sendMessageToThread(ThreadMessageCreateRequestDto dto) {
		switch (dto.getType()) {
			case SIMPLE:
				sendSimpleMessageToThread(dto);
			default:
				throw new NotAvailableMessageTypeException();
		}
	}


	private void sendSimpleMessageToThread(ThreadMessageCreateRequestDto dto) {

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
	public void putMarkOnThreadMessage(ThreadMessageMarkCreateRequestDto dto) {

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
