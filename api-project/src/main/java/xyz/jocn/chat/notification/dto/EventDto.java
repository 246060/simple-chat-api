package xyz.jocn.chat.notification.dto;

import lombok.Data;

@Data
public class EventDto {
	private EvtRoutingDto routing;
	private Object message;
}
