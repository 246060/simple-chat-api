package xyz.jocn.chat.common.pubsub;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

public class RedisMessagePublisher implements MessagePublisher {

	private final RedisTemplate<String, String> redisTemplate;
	private final ChannelTopic topic;

	public RedisMessagePublisher(
		RedisTemplate<String, String> redisTemplate, ChannelTopic topic) {
		this.redisTemplate = redisTemplate;
		this.topic = topic;
	}

	public void emit(EventDto event) {
		redisTemplate.convertAndSend(topic.getTopic(), event);
	}

	public void emit(ChannelTopic topic, EventDto event) {
		redisTemplate.convertAndSend(topic.getTopic(), event);
	}
}
