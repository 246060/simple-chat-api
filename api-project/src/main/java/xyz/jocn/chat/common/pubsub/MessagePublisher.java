package xyz.jocn.chat.common.pubsub;

import org.springframework.data.redis.listener.ChannelTopic;

public interface MessagePublisher {
	void emit(EventDto event);

	void emit(ChannelTopic topic, EventDto event);
}
