package xyz.jocn.chat.notification.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import xyz.jocn.chat.notification.enums.EventType;
import xyz.jocn.chat.message.enums.ChatMessageType;

@Data
public class EvtMessageNewDto {
	@Setter(AccessLevel.NONE)
	private String eventType = EventType.channel_message_new.getEventType();
	private Long channelId;
	private Long messageId;
	private ChatMessageType type;

	public EvtMessageNewDto(Long channelId, Long messageId, ChatMessageType type) {
		this.channelId = channelId;
		this.messageId = messageId;
		this.type = type;
	}
}
