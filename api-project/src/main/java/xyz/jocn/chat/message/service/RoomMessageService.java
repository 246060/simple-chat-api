package xyz.jocn.chat.message.service;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.common.pubsub.EventTarget.*;
import static xyz.jocn.chat.common.pubsub.EventType.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
import xyz.jocn.chat.common.pubsub.EventDto;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.message.converter.RoomMessageReactionConverter;
import xyz.jocn.chat.message.dto.RoomMessageDto;
import xyz.jocn.chat.message.dto.RoomMessageReactionAddRequestDto;
import xyz.jocn.chat.message.dto.RoomMessageReactionDto;
import xyz.jocn.chat.message.dto.RoomMessageSendRequestDto;
import xyz.jocn.chat.message.entity.RoomMessageEntity;
import xyz.jocn.chat.message.entity.RoomMessageReactionEntity;
import xyz.jocn.chat.message.enums.RoomMessageState;
import xyz.jocn.chat.message.repo.room_message.RoomMessageRepository;
import xyz.jocn.chat.message.repo.room_message_reaction.RoomMessageReactionRepository;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.service.RoomParticipantService;
import xyz.jocn.chat.room.RoomEntity;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoomMessageService {

	private final RoomMessageRepository roomMessageRepository;
	private final RoomMessageReactionRepository roomMessageReactionRepository;

	private final RoomParticipantService roomParticipantService;

	private final RoomMessageReactionConverter roomMessageReactionConverter = RoomMessageReactionConverter.INSTANCE;

	private final MessagePublisher publisher;

	@Value("${app.publish-event-trigger}")
	public boolean isPublishEventTrigger;

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public void sendRoomMessage(long uid, long roomId, RoomMessageSendRequestDto dto) {

		roomParticipantService.checkRoomParticipant(roomId, uid);

		switch (dto.getType()) {
			case SHORT_TEXT:
				sendShortMessageToRoom(uid, roomId, dto);
				break;
			case LONG_TEXT:
				sendLongMessageToRoom(uid, roomId, dto);
				break;
			case FILE:
				sendFileMessageToRoom(uid, roomId, dto);
				break;
			default:
				throw new NotAvailableFeatureException();
		}

		if (isPublishEventTrigger) {
			publisher.emit(EventDto.builder()
				.target(ROOM_AREA)
				.type(ROOM_MESSAGE_EVENT)
				.spaceId(roomId)
				.build()
			);
		}
	}

	private void sendShortMessageToRoom(long uid, long roomId, RoomMessageSendRequestDto dto) {

		RoomParticipantEntity participant = roomParticipantService.fetchRoomParticipant(roomId, uid);

		roomMessageRepository.save(
			RoomMessageEntity.builder()
				.type(dto.getType())
				.room(RoomEntity.builder().id(roomId).build())
				.sender(participant)
				.build()
		);
	}

	private void sendFileMessageToRoom(long uid, long roomId, RoomMessageSendRequestDto dto) {

	}

	private void sendLongMessageToRoom(long uid, long roomId, RoomMessageSendRequestDto dto) {

	}

	@Transactional
	public void deleteRoomMessage(long uid, long roomId, long messageId) {

		RoomParticipantEntity roomParticipantEntity = roomParticipantService.fetchRoomParticipant(roomId, uid);

		RoomMessageEntity roomMessageEntity =
			roomMessageRepository
				.findByIdAndSenderId(messageId, roomParticipantEntity.getId())
				.orElseThrow(() -> new ResourceNotFoundException(ROOM_MESSAGE));

		roomMessageEntity.changeState(RoomMessageState.DELETED);

		if (isPublishEventTrigger) {
			publisher.emit(
				EventDto.builder()
					.target(ROOM_AREA)
					.type(ROOM_MESSAGE_EVENT)
					.spaceId(roomMessageEntity.getRoom().getId())
					.build()
			);
		}
	}

	@Transactional
	public void addReaction(long uid, long roomId, long messageId, RoomMessageReactionAddRequestDto dto) {

		RoomParticipantEntity roomParticipantEntity = roomParticipantService.fetchRoomParticipant(roomId, uid);

		RoomMessageEntity roomMessageEntity =
			roomMessageRepository
				.findById(messageId)
				.orElseThrow(() -> new ResourceNotFoundException(ROOM_MESSAGE));

		roomMessageReactionRepository.save(
			RoomMessageReactionEntity.builder()
				.type(dto.getType())
				.roomMessage(roomMessageEntity)
				.roomParticipant(roomParticipantEntity)
				.build()
		);

		if (isPublishEventTrigger) {
			publisher.emit(
				EventDto.builder()
					.target(ROOM_AREA)
					.type(ROOM_MESSAGE_EVENT)
					.spaceId(roomMessageEntity.getRoom().getId())
					.build()
			);
		}
	}

	@Transactional
	public void cancelReaction(long uid, long roomId, long reactionId) {

		RoomParticipantEntity roomParticipantEntity = roomParticipantService.fetchRoomParticipant(roomId, uid);

		RoomMessageReactionEntity roomMessageReactionEntity =
			roomMessageReactionRepository
				.findByIdAndRoomParticipant(reactionId, roomParticipantEntity)
				.orElseThrow(() -> new ResourceNotFoundException(ROOM_MESSAGE_REACTION));

		roomMessageReactionRepository.delete(roomMessageReactionEntity);

		if (isPublishEventTrigger) {
			publisher.emit(
				EventDto.builder()
					.target(ROOM_AREA)
					.type(ROOM_MESSAGE_EVENT)
					.spaceId(roomId)
					.build()
			);
		}
	}

	/*
	 * Query ===============================================================================
	 * */

	public PageDto<RoomMessageDto> fetchRoomMessages(long uid, long roomId, PageMeta pageMeta) {

		roomParticipantService.checkRoomParticipant(roomId, uid);

		PageRequest pageRequest = PageRequest.of(pageMeta.getPageIndex(), pageMeta.getSizePerPage());
		Page<RoomMessageEntity> page = roomMessageRepository.findAllByRoomId(roomId, pageRequest);

		PageMeta meta = new PageMeta();
		meta.setPageIndex(page.getNumber());
		meta.setSizePerPage(page.getNumberOfElements());
		meta.setHasNext(page.hasNext());
		meta.setHasPrev(page.hasPrevious());
		meta.setTotalItems(page.getTotalElements());
		meta.setTotalPages(page.getTotalPages());

		return new PageDto(meta, page.getContent());
	}

	public List<RoomMessageReactionDto> fetchReactions(long uid, long roomId, long messageId) {

		roomParticipantService.checkRoomParticipant(roomId, uid);

		RoomMessageEntity roomMessageEntity =
			roomMessageRepository
				.findById(messageId)
				.orElseThrow(() -> new ResourceNotFoundException(ROOM_MESSAGE));

		return roomMessageReactionConverter.toDto(
			roomMessageReactionRepository.findAllByRoomMessage(roomMessageEntity)
		);
	}

}
