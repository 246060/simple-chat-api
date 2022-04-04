package xyz.jocn.chat.notification.dto;

import lombok.Getter;
import xyz.jocn.chat.notification.enums.RoutingType;

@Getter
public class EventRouting {
	private String type;
	private String targetId;

	public EventRouting(RoutingType type, String targetId) {
		this.type = type.getType();
		this.targetId = targetId;
	}
}
