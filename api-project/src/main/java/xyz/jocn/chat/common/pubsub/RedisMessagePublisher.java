package xyz.jocn.chat.common.pubsub;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RedisMessagePublisher implements MessagePublisher {

	private final RedisTemplate<String, String> redisTemplate;
	private final ChannelTopic topic;

	public void emit(EventDto event) {
		redisTemplate.convertAndSend(topic.getTopic(), event);
	}

	public void emit(ChannelTopic topic, EventDto event) {
		redisTemplate.convertAndSend(topic.getTopic(), event);
	}
}
