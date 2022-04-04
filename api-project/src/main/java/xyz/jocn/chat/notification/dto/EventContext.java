package xyz.jocn.chat.notification.dto;

import lombok.Getter;

@Getter
public class EventContext {
	private EventRouting routing;
	private Object data;

	public EventContext(EventRouting routing, Object data) {
		this.routing = routing;
		this.data = data;
	}
}
