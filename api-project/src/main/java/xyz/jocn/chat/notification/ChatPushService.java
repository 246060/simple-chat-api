package xyz.jocn.chat.notification;

import static xyz.jocn.chat.notification.enums.RoutingType.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.notification.dto.EventContext;
import xyz.jocn.chat.notification.dto.EventDto;
import xyz.jocn.chat.notification.dto.EventRouting;
import xyz.jocn.chat.notification.dto.EvtChannelExit;
import xyz.jocn.chat.notification.dto.EvtChannelJoin;
import xyz.jocn.chat.notification.dto.EvtMessageDel;
import xyz.jocn.chat.notification.dto.EvtMessageNew;
import xyz.jocn.chat.notification.dto.EvtReaction;
import xyz.jocn.chat.notification.dto.EvtUnReadMessage;
import xyz.jocn.chat.user.dto.UserDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatPushService {
	private final MessagePublisher publisher;

	private EventRouting getChannelEventRouting(EventDto dto) {
		return new EventRouting(channel, String.valueOf(dto.getChannelId()));
	}

	public void pushChannelNewMessageEvent(EventDto dto) {
		EvtMessageNew data = new EvtMessageNew(dto.getChannelId(), dto.getMessageId(), dto.getMessageType());
		publisher.emit(new EventContext(getChannelEventRouting(dto), data));
	}

	public void pushChannelMessageDeletedEvent(EventDto dto) {
		EvtMessageDel data = new EvtMessageDel(dto.getChannelId(), dto.getMessageId());
		publisher.emit(new EventContext(getChannelEventRouting(dto), data));
	}

	public void pushChannelReadMessageEvent(EventDto dto) {
		EvtUnReadMessage data = new EvtUnReadMessage(dto.getChannelId(), dto.getMessageId());
		publisher.emit(new EventContext(getChannelEventRouting(dto), data));
	}

	public void pushChannelInviteEvent(EventDto dto) {
		List<UserDto> users = dto.getInvitees().stream()
			.map(userEntity -> {
				UserDto userDto = new UserDto();
				userDto.setId(userEntity.getId());
				userDto.setName(userEntity.getName());
				return userDto;
			}).collect(Collectors.toList());

		EvtChannelJoin data = new EvtChannelJoin(dto.getChannelId(), users);
		publisher.emit(new EventContext(getChannelEventRouting(dto), data));
	}

	public void pushChannelExitEvent(EventDto dto) {
		EvtChannelExit data = new EvtChannelExit(dto.getChannelId(), dto.getUser().getId(), dto.getUser().getName());
		publisher.emit(new EventContext(getChannelEventRouting(dto), data));
	}

	public void pushChannelMessageReactionEvent(EventDto dto) {
		EvtReaction data = new EvtReaction(dto.getChannelId(), dto.getMessageId());
		publisher.emit(new EventContext(getChannelEventRouting(dto), data));
	}
}
