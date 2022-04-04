package xyz.jocn.chat.notification.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import xyz.jocn.chat.notification.enums.EventType;

@Data
public class EvtReaction {

	@Setter(AccessLevel.NONE)
	private String eventType = EventType.channel_message_reaction.getEventType();
	private Long channelId;
	private Long messageId;

	public EvtReaction(Long channelId, Long messageId) {
		this.channelId = channelId;
		this.messageId = messageId;
	}
}
