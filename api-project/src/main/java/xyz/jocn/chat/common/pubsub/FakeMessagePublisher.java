package xyz.jocn.chat.common.pubsub;

import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.notification.dto.EventContext;

@Slf4j
public class FakeMessagePublisher implements MessagePublisher {

	@Override
	public void emit(EventContext eventContext) {
		log.info("pub message : {}", eventContext);
	}

	@Override
	public void emit(TopicType topicType, EventContext eventContext) {
		log.info("topic : {}, pub message : {}", topicType.getTopic(), eventContext);
	}
}
