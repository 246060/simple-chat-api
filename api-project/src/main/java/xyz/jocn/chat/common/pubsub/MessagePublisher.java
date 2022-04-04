package xyz.jocn.chat.common.pubsub;

import xyz.jocn.chat.notification.dto.EventContext;

public interface MessagePublisher {

	void emit(EventContext eventContext);

	void emit(TopicType topicType, EventContext eventContext);
}
