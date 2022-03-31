package xyz.jocn.chat.common.pubsub;

import xyz.jocn.chat.notification.dto.EventDto;

public interface MessagePublisher {

	void emit(EventDto event);

	void emit(TopicType topicType, EventDto event);
}
