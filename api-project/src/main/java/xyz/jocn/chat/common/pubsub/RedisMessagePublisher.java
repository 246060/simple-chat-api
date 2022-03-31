package xyz.jocn.chat.common.pubsub;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;

import xyz.jocn.chat.notification.dto.EventDto;

public class RedisMessagePublisher implements MessagePublisher {

	private final RedisTemplate<String, String> redisTemplate;

	// default publish topic
	private Topic topic;

	public RedisMessagePublisher(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.topic = new ChannelTopic(TopicType.EVENT_API_SERVER.getTopic());
	}

	@Override
	public void emit(EventDto event) {
		redisTemplate.convertAndSend(topic.getTopic(), event);
	}

	@Override
	public void emit(TopicType topicType, EventDto event) {
		Topic topic = new ChannelTopic(topicType.getTopic());
		redisTemplate.convertAndSend(topic.getTopic(), event);
	}
}
