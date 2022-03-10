package xyz.jocn.chat.message.service;

import static xyz.jocn.chat.common.enums.ResourceType.*;
import static xyz.jocn.chat.common.pubsub.EventTarget.*;
import static xyz.jocn.chat.common.pubsub.EventType.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.dto.PageDto;
import xyz.jocn.chat.common.dto.PageMeta;
import xyz.jocn.chat.common.exception.NotAvailableFeatureException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.common.pubsub.ChatProducer;
import xyz.jocn.chat.common.pubsub.PublishEvent;
import xyz.jocn.chat.message.converter.RoomMessageMarkConverter;
import xyz.jocn.chat.message.dto.RoomMessageChangeDto;
import xyz.jocn.chat.message.dto.RoomMessageDto;
import xyz.jocn.chat.message.dto.RoomMessageGetDto;
import xyz.jocn.chat.message.dto.RoomMessageMarkCreateDto;
import xyz.jocn.chat.message.dto.RoomMessageMarkDto;
import xyz.jocn.chat.message.dto.RoomMessageSendDto;
import xyz.jocn.chat.message.entity.RoomMessageEntity;
import xyz.jocn.chat.message.entity.RoomMessageMarkEntity;
import xyz.jocn.chat.message.repo.room_message.RoomMessageRepository;
import xyz.jocn.chat.message.repo.room_message_mark.RoomMessageMarkRepository;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.repo.room_participant.RoomParticipantRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomMessageService {

	private final RoomParticipantRepository roomParticipantRepository;
	private final RoomMessageRepository roomMessageRepository;
	private final RoomMessageMarkRepository roomMessageMarkRepository;

	private final RoomMessageMarkConverter roomMessageMarkConverter = RoomMessageMarkConverter.INSTANCE;

	private final ChatProducer chatProducer;

	@Transactional
	public void sendMessageToRoom(RoomMessageSendDto dto) {
		switch (dto.getType()) {
			case SIMPLE:
				sendSimpleMessageToRoom(dto);
				break;
			default:
				throw new NotAvailableFeatureException();
		}

		PublishEvent publishEvent = new PublishEvent();
		publishEvent.setTarget(ROOM_AREA);
		publishEvent.setType(ROOM_MESSAGE_EVENT);
		publishEvent.setSpaceId(dto.getRoomId());
		chatProducer.emit(publishEvent);
	}

	private void sendSimpleMessageToRoom(RoomMessageSendDto dto) {

		RoomParticipantEntity roomParticipantEntity =
			roomParticipantRepository
				.findByRoomIdAndUserId(dto.getRoomId(), dto.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException(ROOM_PARTICIPANT));

		roomMessageRepository.save(
			RoomMessageEntity.builder()
				.type(dto.getType())
				.room(roomParticipantEntity.getRoom())
				.sender(roomParticipantEntity)
				.build()
		);
	}

	public PageDto<RoomMessageDto> getMessagesInRoom(RoomMessageGetDto dto, PageMeta pageMeta) {

		PageRequest pageRequest = PageRequest.of(pageMeta.getPageIndex(), pageMeta.getSizePerPage());
		Page<RoomMessageEntity> page = roomMessageRepository.findAllByRoomId(dto.getRoomId(), pageRequest);

		PageMeta meta = new PageMeta();
		meta.setPageIndex(page.getNumber());
		meta.setSizePerPage(page.getNumberOfElements());
		meta.setHasNext(page.hasNext());
		meta.setHasPrev(page.hasPrevious());
		meta.setTotalItems(page.getTotalElements());
		meta.setTotalPages(page.getTotalPages());

		return new PageDto(meta, page.getContent());
	}

	@Transactional
	public void mark(RoomMessageMarkCreateDto dto) {

		RoomMessageEntity roomMessageEntity =
			roomMessageRepository
				.findById(dto.getMessageId())
				.orElseThrow(() -> new ResourceNotFoundException(ROOM_MESSAGE));

		RoomParticipantEntity roomParticipantEntity =
			roomParticipantRepository
				.findByRoomIdAndUserId(roomMessageEntity.getRoom().getId(), dto.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException(ROOM_PARTICIPANT));

		roomMessageMarkRepository.save(
			RoomMessageMarkEntity.builder()
				.flag(dto.getType())
				.roomMessage(roomMessageEntity)
				.roomParticipant(roomParticipantEntity)
				.build()
		);

		PublishEvent publishEvent = new PublishEvent();
		publishEvent.setTarget(ROOM_AREA);
		publishEvent.setType(ROOM_MESSAGE_EVENT);
		publishEvent.setSpaceId(roomMessageEntity.getRoom().getId());
		chatProducer.emit(publishEvent);
	}

	public List<RoomMessageMarkDto> getMarks(Long messageId) {

		RoomMessageEntity roomMessageEntity =
			roomMessageRepository.findById(messageId).orElseThrow(() -> new ResourceNotFoundException(ROOM_MESSAGE));

		List<RoomMessageMarkEntity> roomMessageMarkEntities =
			roomMessageMarkRepository.findAllByRoomMessage(roomMessageEntity);

		return roomMessageMarkConverter.toDto(roomMessageMarkEntities);
	}

	@Transactional
	public void cancelMark(Long markId) {

		RoomMessageMarkEntity roomMessageMarkEntity =
			roomMessageMarkRepository
				.findById(markId)
				.orElseThrow(() -> new ResourceNotFoundException(THREAD_MESSAGE_MARK));

		long roomId = roomMessageMarkEntity.getRoomParticipant().getRoom().getId();

		roomMessageMarkRepository.delete(roomMessageMarkEntity);

		PublishEvent publishEvent = new PublishEvent();
		publishEvent.setTarget(ROOM_AREA);
		publishEvent.setType(ROOM_MESSAGE_EVENT);
		publishEvent.setSpaceId(roomId);
		chatProducer.emit(publishEvent);
	}

	@Transactional
	public void change(RoomMessageChangeDto dto) {

		RoomMessageEntity roomMessageEntity =
			roomMessageRepository
				.findById(dto.getMessageId())
				.orElseThrow(() -> new ResourceNotFoundException(ROOM_MESSAGE));

		roomMessageEntity.changeState(dto.getState());

		PublishEvent publishEvent = new PublishEvent();
		publishEvent.setTarget(ROOM_AREA);
		publishEvent.setType(ROOM_MESSAGE_EVENT);
		publishEvent.setSpaceId(roomMessageEntity.getRoom().getId());
		chatProducer.emit(publishEvent);
	}
}
