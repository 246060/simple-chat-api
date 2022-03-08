package xyz.jocn.chat.message.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.chat_space.entity.RoomEntity;
import xyz.jocn.chat.chat_space.exception.NotFoundRoomException;
import xyz.jocn.chat.chat_space.repo.room.RoomRepository;
import xyz.jocn.chat.common.dto.PageDto;
import xyz.jocn.chat.message.converter.RoomMessageMarkConverter;
import xyz.jocn.chat.message.dto.RoomMessageDto;
import xyz.jocn.chat.message.dto.RoomMessageGetDto;
import xyz.jocn.chat.message.dto.RoomMessageMarkCreateDto;
import xyz.jocn.chat.message.dto.RoomMessageMarkDto;
import xyz.jocn.chat.message.dto.RoomMessageSendDto;
import xyz.jocn.chat.message.entity.RoomMessageEntity;
import xyz.jocn.chat.message.entity.RoomMessageMarkEntity;
import xyz.jocn.chat.message.exception.NotAvailableMessageTypeException;
import xyz.jocn.chat.message.exception.NotFoundRoomMessageException;
import xyz.jocn.chat.message.repo.room_message_mark.RoomMessageMarkRepository;
import xyz.jocn.chat.message.repo.room_message.RoomMessageRepository;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.exception.NotFoundRoomParticipantException;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomMessageService {

	private final RoomRepository roomRepository;
	private final RoomParticipantRepository roomParticipantRepository;
	private final RoomMessageRepository roomMessageRepository;
	private final RoomMessageMarkRepository roomMessageMarkRepository;

	private final RoomMessageMarkConverter roomMessageMarkConverter = RoomMessageMarkConverter.INSTANCE;

	@Transactional
	public void sendMessageToRoom(RoomMessageSendDto dto) {
		switch (dto.getType()) {
			case SIMPLE:
				sendSimpleMessageToRoom(dto);
			default:
				throw new NotAvailableMessageTypeException();
		}

		// ProducerEvent producerEvent = new ProducerEvent();
		// producer.emit(producerEvent);
	}

	private void sendSimpleMessageToRoom(RoomMessageSendDto dto) {
		RoomEntity roomEntity =
			roomRepository
				.findById(dto.getRoomId())
				.orElseThrow(NotFoundRoomException::new);

		RoomParticipantEntity roomParticipantEntity =
			roomParticipantRepository
				.findById(dto.getParticipantId())
				.orElseThrow(NotFoundRoomParticipantException::new);

		RoomMessageEntity roomMessageEntity =
			RoomMessageEntity.builder()
				.type(dto.getType())
				.room(roomEntity)
				.sender(roomParticipantEntity)
				.build();

		roomMessageRepository.save(roomMessageEntity);
	}

	public PageDto<RoomMessageDto> getMessagesInRoom(RoomMessageGetDto dto) {

		return null;
	}

	@Transactional
	public void putMarkOnRoomMessage(RoomMessageMarkCreateDto dto) {

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
}
