package xyz.jocn.chat.message;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.message.enums.ChatMessageType.FILE;
import static xyz.jocn.chat.message.enums.ChatMessageType.*;
import static xyz.jocn.chat.message.enums.MessageState.*;
import static xyz.jocn.chat.participant.ParticipantState.*;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.channel.ChannelEntity;
import xyz.jocn.chat.channel.repo.ChannelRepository;
import xyz.jocn.chat.common.dto.PageDto;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.common.util.StringUtil;
import xyz.jocn.chat.file.FileEntity;
import xyz.jocn.chat.file.FileService;
import xyz.jocn.chat.message.dto.MessageDto;
import xyz.jocn.chat.message.dto.MessageSendRequestDto;
import xyz.jocn.chat.message.entity.MessageEntity;
import xyz.jocn.chat.message.entity.MessageFileEntity;
import xyz.jocn.chat.message.repo.MessageRepository;
import xyz.jocn.chat.message.repo.message_file.MessageFileRepository;
import xyz.jocn.chat.notification.ChatPushService;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.ParticipantService;
import xyz.jocn.chat.participant.repo.ParticipantRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MessageService {

	private final MessageRepository messageRepository;
	private final MessageFileRepository messageFileRepository;
	private final ParticipantRepository participantRepository;
	private final ChannelRepository channelRepository;

	private final ParticipantService participantService;
	private final FileService fileService;
	private final ChatPushService chatPushService;

	private final int MESSAGE_LIMIT_LENGTH = 500;

	private boolean haveFile(MessageSendRequestDto dto) {
		if (Objects.isNull(dto.getFile())) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isShortTextMessage(String message) {
		return StringUtil.isNotBlank(message) && message.length() <= MESSAGE_LIMIT_LENGTH;
	}

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public void sendMessage(long uid, long channelId, MessageSendRequestDto dto) {

		participantService.checkParticipant(channelId, uid);

		if (haveFile(dto)) {
			sendFileMessage(uid, channelId, dto);
		} else {
			if (isShortTextMessage(dto.getMessage())) {
				sendShortMessage(uid, channelId, dto);
			} else {
				sendLongMessage(uid, channelId, dto);
			}
		}
	}

	private void sendShortMessage(long uid, long channelId, MessageSendRequestDto dto) {

		ParticipantEntity participant = participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		ChannelEntity channelEntity = channelRepository
			.findById(channelId)
			.orElseThrow(() -> new ResourceNotFoundException(CHANNEL));

		MessageEntity messageEntity = MessageEntity.builder()
			.channel(channelEntity)
			.message(dto.getMessage())
			.sender(participant)
			.type(SHORT_TEXT)
			.build();

		messageRepository.save(messageEntity);
		channelEntity.registerPivotMessage(messageEntity.getId());
		chatPushService.pushChannelNewMessageEvent(channelId, messageEntity.getId(), SHORT_TEXT);
	}

	private void sendLongMessage(long uid, long channelId, MessageSendRequestDto dto) {

		ParticipantEntity participant = participantService.fetchParticipant(channelId, uid);

		MessageEntity messageEntity = MessageEntity.builder()
			.channel(ChannelEntity.builder().id(channelId).build())
			.message(String.format("%s...", dto.getMessage().substring(0, MESSAGE_LIMIT_LENGTH)))
			.sender(participant)
			.type(LONG_TEXT)
			.build();

		messageRepository.save(messageEntity);
		FileEntity fileEntity = fileService.save(uid, dto.getMessage());
		messageFileRepository.save(MessageFileEntity.builder().message(messageEntity).file(fileEntity).build());
		chatPushService.pushChannelNewMessageEvent(channelId, messageEntity.getId(), LONG_TEXT);
	}

	private void sendFileMessage(long uid, long channelId, MessageSendRequestDto dto) {
		ParticipantEntity participant = participantService.fetchParticipant(channelId, uid);

		MessageEntity messageEntity = MessageEntity.builder()
			.channel(ChannelEntity.builder().id(channelId).build())
			.message(null)
			.sender(participant)
			.type(FILE)
			.build();

		messageRepository.save(messageEntity);
		FileEntity fileEntity = fileService.save(dto.getFile(), uid);
		messageFileRepository.save(MessageFileEntity.builder().message(messageEntity).file(fileEntity).build());
		chatPushService.pushChannelNewMessageEvent(channelId, messageEntity.getId(), FILE);
	}

	@Transactional
	public void deleteMessage(long uid, long channelId, long messageId) {

		ParticipantEntity participantEntity = participantService.fetchParticipant(channelId, uid);

		MessageEntity messageEntity = messageRepository
			.findByIdAndSenderId(messageId, participantEntity.getId())
			.orElseThrow(() -> new ResourceNotFoundException(MESSAGE));

		messageEntity.changeState(DELETED);
		chatPushService.pushChannelMessageDeletedEvent(channelId, messageEntity.getId());
	}

	/*
	 * Query ===============================================================================
	 * */

	public PageDto<MessageDto> fetchMessages(long uid, long channelId) {

		participantService.checkParticipant(channelId, uid);

		// PageMeta meta = new PageMeta();
		// meta.setPageIndex(page.getNumber());
		// meta.setSizePerPage(page.getNumberOfElements());
		// meta.setHasNext(page.hasNext());
		// meta.setHasPrev(page.hasPrevious());
		// meta.setTotalItems(page.getTotalElements());
		// meta.setTotalPages(page.getTotalPages());
		//
		// return new PageDto(meta, page.getContent());

		return null;
	}

}
