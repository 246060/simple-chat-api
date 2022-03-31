package xyz.jocn.chat.notification.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import xyz.jocn.chat.notification.enums.EventType;

@Getter
public class EvtAllAdDto {
	@Setter(AccessLevel.NONE)
	private String eventType = EventType.all_ad.getEventType();

}
