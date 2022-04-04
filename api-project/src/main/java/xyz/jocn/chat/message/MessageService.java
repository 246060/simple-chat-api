package xyz.jocn.chat.message;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.message.enums.MessageType.*;
import static xyz.jocn.chat.participant.ParticipantState.*;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.channel.ChannelEntity;
import xyz.jocn.chat.channel.repo.ChannelRepository;
import xyz.jocn.chat.common.dto.SliceCriteria;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.common.util.StringUtil;
import xyz.jocn.chat.file.FileMetaEntity;
import xyz.jocn.chat.file.FileService;
import xyz.jocn.chat.message.dto.MessageDto;
import xyz.jocn.chat.message.dto.MessageSendRequestDto;
import xyz.jocn.chat.message.repo.MessageRepository;
import xyz.jocn.chat.notification.ChatPushService;
import xyz.jocn.chat.notification.dto.EventDto;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.ParticipantService;
import xyz.jocn.chat.participant.repo.ParticipantRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MessageService {

	private final MessageRepository messageRepository;
	private final ParticipantRepository participantRepository;
	private final ChannelRepository channelRepository;

	private final FileService fileService;
	private final ChatPushService chatPushService;

	private MessageConverter messageConverter = MessageConverter.INSTANCE;

	private final int MESSAGE_LIMIT_LENGTH = 500;

	private boolean haveFile(MessageSendRequestDto dto) {
		if (Objects.isNull(dto.getFile())) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isShortTextMessage(String message) {
		return message.length() <= MESSAGE_LIMIT_LENGTH;
	}

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public void sendMessage(long uid, long channelId, MessageSendRequestDto dto) {

		participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		if (haveFile(dto)) {
			sendFileMessage(uid, channelId, dto);
			return;
		}

		if (StringUtil.isBlank(dto.getMessage())) {
			throw new IllegalArgumentException("if message type is not file, must have text");
		}

		if (isShortTextMessage(dto.getMessage())) {
			sendShortMessage(uid, channelId, dto);
		} else {
			sendLongMessage(uid, channelId, dto);
		}
	}

	private void sendShortMessage(long uid, long channelId, MessageSendRequestDto dto) {

		ParticipantEntity participant = participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		ChannelEntity channel = channelRepository
			.findById(channelId)
			.orElseThrow(() -> new ResourceNotFoundException(CHANNEL));

		MessageEntity message = MessageEntity.builder()
			.channel(channel)
			.text(dto.getMessage())
			.sender(participant)
			.unreadCount(channel.getNumberOfParticipants())
			.type(short_text)
			.parentId(dto.getParentId())
			.build();

		messageRepository.save(message);
		channel.saveFirstMessageId(message.getId());

		chatPushService.pushChannelNewMessageEvent(EventDto.builder()
			.channelId(channelId)
			.messageId(message.getId())
			.messageType(short_text)
			.mentions(dto.getMentions())
			.build()
		);
	}

	private void sendLongMessage(long uid, long channelId, MessageSendRequestDto dto) {

		ParticipantEntity participant = participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		ChannelEntity channel = channelRepository
			.findById(channelId)
			.orElseThrow(() -> new ResourceNotFoundException(CHANNEL));

		FileMetaEntity fileMeta = fileService.save(uid, dto.getMessage());

		MessageEntity message = MessageEntity.builder()
			.channel(channel)
			.text(String.format("%s...", dto.getMessage().substring(0, MESSAGE_LIMIT_LENGTH)))
			.sender(participant)
			.file(fileMeta)
			.type(long_text)
			.parentId(dto.getParentId())
			.unreadCount(channel.getNumberOfParticipants())
			.build();

		messageRepository.save(message);
		channel.saveFirstMessageId(message.getId());

		chatPushService.pushChannelNewMessageEvent(EventDto.builder()
			.channelId(channelId)
			.messageId(message.getId())
			.messageType(long_text)
			.build()
		);
	}

	private void sendFileMessage(long uid, long channelId, MessageSendRequestDto dto) {
		ParticipantEntity participant = participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		ChannelEntity channel = channelRepository
			.findById(channelId)
			.orElseThrow(() -> new ResourceNotFoundException(CHANNEL));

		FileMetaEntity fileMeta = fileService.save(dto.getFile(), uid);

		MessageEntity message = MessageEntity.builder()
			.channel(channel)
			.text(null)
			.sender(participant)
			.type(file)
			.file(fileMeta)
			.parentId(dto.getParentId())
			.unreadCount(channel.getNumberOfParticipants())
			.build();

		messageRepository.save(message);
		channel.saveFirstMessageId(message.getId());

		chatPushService.pushChannelNewMessageEvent(EventDto.builder()
			.channelId(channelId)
			.messageId(message.getId())
			.messageType(file)
			.build()
		);
	}

	@Transactional
	public void deleteMessage(long uid, long channelId, long messageId) {

		ParticipantEntity participant = participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		MessageEntity message = messageRepository
			.findByIdAndSenderId(messageId, participant.getId())
			.orElseThrow(() -> new ResourceNotFoundException(MESSAGE));

		message.delete();

		chatPushService.pushChannelMessageDeletedEvent(EventDto.builder()
			.channelId(channelId)
			.messageId(message.getId())
			.build()
		);
	}

	/*
	 * Query ===============================================================================
	 * */

	@Transactional
	public List<MessageDto> fetchMessages(long uid, long channelId, SliceCriteria criteria) {

		ParticipantEntity participant = participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		List<MessageEntity> messages = messageRepository.findMessagesInChannel(channelId, criteria);

		Long lastReadMessageId = participant.getLastReadMessageId();
		for (MessageEntity message : messages) {
			if (participant.getLastReadMessageId() == null ||
				participant.getLastReadMessageId() < message.getId()) {
				message.unReadCountDown();
				lastReadMessageId = Math.max(lastReadMessageId, message.getId());
			}
		}

		participant.updateCurrentLastReadMessageId(lastReadMessageId);
		return messageConverter.toDto(messages);
	}

}
