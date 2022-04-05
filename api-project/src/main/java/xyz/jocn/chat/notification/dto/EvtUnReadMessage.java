package xyz.jocn.chat.notification.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import xyz.jocn.chat.notification.enums.EventType;

@Data
public class EvtUnReadMessage {

	@Setter(AccessLevel.NONE)
	private String eventType = EventType.channel_message_read.getEventType();

	private Long channelId;
	private Long messageId;

	public EvtUnReadMessage(Long channelId, Long messageId) {
		this.channelId = channelId;
		this.messageId = messageId;
	}
}
