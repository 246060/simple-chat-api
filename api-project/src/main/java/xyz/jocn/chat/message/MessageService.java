package xyz.jocn.chat.message;

import static xyz.jocn.chat.common.exception.ResourceType.*;
import static xyz.jocn.chat.message.enums.MessageType.*;
import static xyz.jocn.chat.participant.ParticipantState.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.channel.ChannelEntity;
import xyz.jocn.chat.channel.repo.ChannelRepository;
import xyz.jocn.chat.common.dto.SliceCriteria;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.file.FileMetaEntity;
import xyz.jocn.chat.file.FileService;
import xyz.jocn.chat.message.dto.MessageDto;
import xyz.jocn.chat.message.dto.MessageFileSendRequestDto;
import xyz.jocn.chat.message.dto.MessageTextSendRequestDto;
import xyz.jocn.chat.message.repo.MessageRepository;
import xyz.jocn.chat.message.repo.unread_message.UnReadMessageRepository;
import xyz.jocn.chat.notification.ChatPushService;
import xyz.jocn.chat.notification.dto.EventDto;
import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.repo.ParticipantRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MessageService {

	private final MessageRepository messageRepository;
	private final ParticipantRepository participantRepository;
	private final ChannelRepository channelRepository;
	private final UnReadMessageRepository unReadMessageRepository;

	private final FileService fileService;
	private final ChatPushService chatPushService;

	private MessageConverter messageConverter = MessageConverter.INSTANCE;

	private final int MESSAGE_LIMIT_LENGTH = 500;

	private boolean isShortTextMessage(String message) {
		return message.length() <= MESSAGE_LIMIT_LENGTH;
	}

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public void sendTextMessage(long uid, long channelId, MessageTextSendRequestDto dto) {

		ParticipantEntity participant = participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		ChannelEntity channel = channelRepository
			.findById(channelId)
			.orElseThrow(() -> new ResourceNotFoundException(CHANNEL));

		if (isShortTextMessage(dto.getMessage())) {
			sendShortMessage(uid, dto, channel, participant);
		} else {
			sendLongMessage(uid, dto, channel, participant);
		}
	}

	private void sendShortMessage(
		long uid,
		MessageTextSendRequestDto dto,
		ChannelEntity channel,
		ParticipantEntity participant
	) {
		MessageEntity message = MessageEntity.builder()
			.channel(channel)
			.text(dto.getMessage())
			.sender(participant)
			.type(short_text)
			.parentId(dto.getParentId())
			.unreadCount(channel.getNumberOfParticipants())
			.build();

		messageRepository.save(message);
		channel.saveFirstMessageId(message.getId());

		chatPushService.pushChannelNewMessageEvent(EventDto.builder()
			.channelId(channel.getId())
			.messageId(message.getId())
			.messageType(short_text)
			.mentions(dto.getMentions())
			.build()
		);
	}

	private void sendLongMessage(
		long uid,
		MessageTextSendRequestDto dto,
		ChannelEntity channel,
		ParticipantEntity participant
	) {
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
			.channelId(channel.getId())
			.messageId(message.getId())
			.messageType(long_text)
			.build()
		);
	}

	@Transactional
	public void sendFileMessage(long uid, long channelId, MessageFileSendRequestDto dto) {

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
		unReadMessageRepository.deleteByMessageId(messageId);

		chatPushService.pushChannelMessageDeletedEvent(EventDto.builder()
			.channelId(channelId)
			.messageId(message.getId())
			.build()
		);
	}

	@Transactional
	public void updateUnReadCount(long channelId, long messageId, long uid) {

		ParticipantEntity participant = participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		MessageEntity message = messageRepository
			.findById(messageId)
			.orElseThrow(() -> new ResourceNotFoundException(MESSAGE));

		unReadMessageRepository.deleteByMessageIdAndParticipantId(messageId, participant.getId());
		Long unReadCount = unReadMessageRepository.countByMessageId(messageId).orElseGet(() -> 0L);
		message.updateUnReadCount(unReadCount.intValue());

		chatPushService.pushChannelReadMessageEvent(EventDto.builder()
			.channelId(channelId)
			.messageId(message.getId())
			.build()
		);
	}

	/*
	 * Query ===============================================================================
	 * */

	public List<MessageDto> fetchMessages(long uid, long channelId, SliceCriteria criteria) {
		participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		return messageRepository.findMessageDtosInChannel(channelId, criteria);
	}

	public MessageDto fetchMessage(long uid, long channelId, long messageId) {
		participantRepository
			.findByChannelIdAndUserIdAndState(channelId, uid, JOIN)
			.orElseThrow(() -> new ResourceNotFoundException(PARTICIPANT));

		return messageConverter.toDto(
			messageRepository.findById(messageId).orElseThrow(() -> new ResourceNotFoundException(MESSAGE))
		);
	}
}
