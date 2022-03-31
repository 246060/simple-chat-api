package xyz.jocn.chat.common.pubsub;

import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.notification.dto.EventDto;

@Slf4j
public class FakeMessagePublisher implements MessagePublisher {

	@Override
	public void emit(EventDto event) {
		log.info("pub message : {}", event);
	}

	@Override
	public void emit(TopicType topicType, EventDto event) {
		log.info("topic : {}, pub message : {}", topicType.getTopic(), event);
	}
}
