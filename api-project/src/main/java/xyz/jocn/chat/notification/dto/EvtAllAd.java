package xyz.jocn.chat.notification.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import xyz.jocn.chat.notification.enums.EventType;

@Data
public class EvtAllAd {
	@Setter(AccessLevel.NONE)
	private String eventType = EventType.all_ad.getEventType();
}
