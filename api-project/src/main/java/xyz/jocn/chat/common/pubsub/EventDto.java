package xyz.jocn.chat.common.pubsub;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
public class EventDto {
	private Long id;
	private Long spaceId;
	private EventTarget target;
	private EventType type;
	private String message;
	private List<Object> receiver;

	@Builder
	public EventDto(Long id, Long spaceId, EventTarget target, EventType type, String message,
		List<Object> receiver) {
		this.id = id;
		this.spaceId = spaceId;
		this.target = target;
		this.type = type;
		this.message = message;
		this.receiver = receiver;
	}
}
