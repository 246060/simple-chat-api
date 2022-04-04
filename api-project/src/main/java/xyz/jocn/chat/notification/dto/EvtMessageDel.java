package xyz.jocn.chat.notification.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import xyz.jocn.chat.notification.enums.EventType;

@Data
public class EvtMessageDel {

	@Setter(AccessLevel.NONE)
	private String eventType = EventType.channel_message_deleted.getEventType();
	private Long channelId;
	private Long messageId;

	public EvtMessageDel(Long channelId, Long messageId) {
		this.channelId = channelId;
		this.messageId = messageId;
	}
}
