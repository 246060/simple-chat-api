package xyz.jocn.chat.notification.dto;

import lombok.Getter;
import xyz.jocn.chat.notification.enums.RoutingType;

@Getter
public class EvtRoutingDto {
	private String type;
	private String targetId;

	public EvtRoutingDto(RoutingType type, String targetId) {
		this.type = type.getType();
		this.targetId = targetId;
	}
}
