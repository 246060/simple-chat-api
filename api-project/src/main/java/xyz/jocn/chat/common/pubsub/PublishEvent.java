package xyz.jocn.chat.common.pubsub;

import java.util.List;

import lombok.Data;

@Data
public class PublishEvent {
	private EventTarget target;
	private long spaceId;
	private EventType type;
	private List<Object> receiver;
}
