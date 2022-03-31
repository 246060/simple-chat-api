package xyz.jocn.chat.notification;

import static xyz.jocn.chat.notification.enums.RoutingType.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.message.enums.ChatMessageType;
import xyz.jocn.chat.notification.dto.EventDto;
import xyz.jocn.chat.notification.dto.EvtChannelExitDto;
import xyz.jocn.chat.notification.dto.EvtChannelJoinDto;
import xyz.jocn.chat.notification.dto.EvtMessageDelDto;
import xyz.jocn.chat.notification.dto.EvtMessageNewDto;
import xyz.jocn.chat.notification.dto.EvtRoutingDto;
import xyz.jocn.chat.participant.repo.ParticipantRepository;
import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.dto.UserDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatPushService {
	private final MessagePublisher publisher;

	public void pushChannelNewMessageEvent(long channelId, long messageId, ChatMessageType messageType) {
		EventDto event = new EventDto();
		event.setRouting(new EvtRoutingDto(channel, String.valueOf(channelId)));
		event.setMessage(new EvtMessageNewDto(channelId, messageId, messageType));
		publisher.emit(event);
	}

	public void pushChannelMessageDeletedEvent(long channelId, long messageId) {
		EventDto event = new EventDto();
		event.setRouting(new EvtRoutingDto(channel, String.valueOf(channelId)));
		event.setMessage(new EvtMessageDelDto(channelId, messageId));
		publisher.emit(event);
	}

	public void pushChannelInviteEvent(long channelId, List<UserEntity> invitees) {
		List<UserDto> users = invitees.stream()
			.map(userEntity -> {
				UserDto userDto = new UserDto();
				userDto.setId(userEntity.getId());
				userDto.setName(userEntity.getName());
				return userDto;
			}).collect(Collectors.toList());

		EventDto event = new EventDto();
		event.setRouting(new EvtRoutingDto(channel, String.valueOf(channelId)));
		event.setMessage(new EvtChannelJoinDto(channelId, users));
		publisher.emit(event);
	}

	public void pushChannelExitEvent(Long channelId, UserEntity exitUser) {
		EventDto event = new EventDto();
		event.setRouting(new EvtRoutingDto(channel, String.valueOf(channelId)));
		event.setMessage(new EvtChannelExitDto(channelId, exitUser.getId(), exitUser.getName()));
		publisher.emit(event);
	}

}
