package xyz.jocn.chat.message;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.common.pubsub.EventTarget.*;
import static xyz.jocn.chat.common.pubsub.EventType.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.channel.ChannelEntity;
import xyz.jocn.chat.common.dto.PageDto;
import xyz.jocn.chat.common.dto.PageMeta;
import xyz.jocn.chat.common.exception.NotAvailableFeatureException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.common.pubsub.EventDto;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.message.dto.MessageDto;
import xyz.jocn.chat.message.dto.MessageSendRequestDto;
import xyz.jocn.chat.message.entity.MessageEntity;
import xyz.jocn.chat.message.enums.MessageState;
import xyz.jocn.chat.message.repo.message.MessageRepository;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.ParticipantService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MessageService {

	private final MessageRepository messageRepository;
	private final ParticipantService participantService;

	private final MessagePublisher publisher;

	@Value("${app.publish-event-trigger}")
	public boolean isPublishEventTrigger;

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public void sendMessage(long uid, long channelId, MessageSendRequestDto dto) {

		participantService.checkParticipant(channelId, uid);

		switch (dto.getType()) {
			case SHORT_TEXT:
				sendShortMessage(uid, channelId, dto);
				break;
			case LONG_TEXT:
				sendLongMessage(uid, channelId, dto);
				break;
			case FILE:
				sendFileMessage(uid, channelId, dto);
				break;
			default:
				throw new NotAvailableFeatureException();
		}

		if (isPublishEventTrigger) {
			publisher.emit(EventDto.builder()
				.target(ROOM_AREA)
				.type(ROOM_MESSAGE_EVENT)
				.spaceId(channelId)
				.build()
			);
		}
	}

	private void sendShortMessage(long uid, long channelId, MessageSendRequestDto dto) {

		ParticipantEntity participant = participantService.fetchParticipant(channelId, uid);

		messageRepository.save(
			MessageEntity.builder()
				.type(dto.getType())
				.channel(ChannelEntity.builder().id(channelId).build())
				.sender(participant)
				.build()
		);
	}

	private void sendFileMessage(long uid, long channelId, MessageSendRequestDto dto) {

	}

	private void sendLongMessage(long uid, long channelId, MessageSendRequestDto dto) {

	}

	@Transactional
	public void deleteMessage(long uid, long channelId, long messageId) {

		ParticipantEntity participantEntity = participantService.fetchParticipant(channelId, uid);

		MessageEntity messageEntity = messageRepository
			.findByIdAndSenderId(messageId, participantEntity.getId())
			.orElseThrow(() -> new ResourceNotFoundException(CHANNEL_MESSAGE));

		messageEntity.changeState(MessageState.DELETED);

		if (isPublishEventTrigger) {
			publisher.emit(
				EventDto.builder()
					.target(ROOM_AREA)
					.type(ROOM_MESSAGE_EVENT)
					.spaceId(messageEntity.getChannel().getId())
					.build()
			);
		}
	}

	/*
	 * Query ===============================================================================
	 * */

	public PageDto<MessageDto> fetchMessages(long uid, long channelId, PageMeta pageMeta) {

		participantService.checkParticipant(channelId, uid);

		PageRequest pageRequest = PageRequest.of(pageMeta.getPageIndex(), pageMeta.getSizePerPage());
		Page<MessageEntity> page = messageRepository.findAllByChannelId(channelId, pageRequest);

		PageMeta meta = new PageMeta();
		meta.setPageIndex(page.getNumber());
		meta.setSizePerPage(page.getNumberOfElements());
		meta.setHasNext(page.hasNext());
		meta.setHasPrev(page.hasPrevious());
		meta.setTotalItems(page.getTotalElements());
		meta.setTotalPages(page.getTotalPages());

		return new PageDto(meta, page.getContent());
	}

}
