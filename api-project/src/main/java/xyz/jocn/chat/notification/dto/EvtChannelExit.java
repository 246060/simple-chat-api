package xyz.jocn.chat.notification.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import xyz.jocn.chat.notification.enums.EventType;

@Data
public class EvtChannelExit {
	@Setter(AccessLevel.NONE)
	private String eventType = EventType.channel_exit.getEventType();
	private Long channelId;
	private Long userId;
	private String name;

	public EvtChannelExit(Long channelId, Long userId, String name) {
		this.channelId = channelId;
		this.userId = userId;
		this.name = name;
	}
}
