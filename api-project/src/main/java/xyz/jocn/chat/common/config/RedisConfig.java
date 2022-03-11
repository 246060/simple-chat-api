package xyz.jocn.chat.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import xyz.jocn.chat.common.pubsub.EventDto;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.common.pubsub.RedisMessagePublisher;

@Configuration
public class RedisConfig {

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(
		RedisConnectionFactory redisConnectionFactory,
		MessageListenerAdapter messageListenerAdapter
	) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener(messageListenerAdapter, topic());
		return container;
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
		return lettuceConnectionFactory;
	}

	@Bean
	public ChannelTopic topic() {
		final String TopicName = "topic";
		return new ChannelTopic(TopicName);
	}

	@Bean
	public MessageListenerAdapter messageListenerAdapter(MessageListener messageListener) {
		return new MessageListenerAdapter(messageListener);
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		final RedisTemplate<String, String> template = new RedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		// template.setValueSerializer(new GenericToStringSerializer(Object.class));
		template.setValueSerializer(new Jackson2JsonRedisSerializer<>(EventDto.class));
		return template;
	}

	@Bean
	public MessagePublisher redisPublisher(RedisTemplate redisTemplate) {
		return new RedisMessagePublisher(redisTemplate, topic());
	}

}
