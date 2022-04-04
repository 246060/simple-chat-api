package xyz.jocn.chat.notification.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import xyz.jocn.chat.message.enums.MessageType;
import xyz.jocn.chat.notification.enums.EventType;

@Data
public class EvtMessageNew {
	@Setter(AccessLevel.NONE)
	private String eventType = EventType.channel_message_new.getEventType();
	private Long channelId;
	private Long messageId;
	private MessageType type;

	public EvtMessageNew(Long channelId, Long messageId, MessageType type) {
		this.channelId = channelId;
		this.messageId = messageId;
		this.type = type;
	}
}
