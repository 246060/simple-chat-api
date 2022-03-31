package xyz.jocn.chat.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.pubsub.FakeMessagePublisher;
import xyz.jocn.chat.common.pubsub.TopicType;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.common.pubsub.RedisMessagePublisher;
import xyz.jocn.chat.notification.dto.EventDto;

@Slf4j
@Configuration
public class RedisConfig {

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(
		RedisConnectionFactory redisConnectionFactory,
		MessageListenerAdapter messageListenerAdapter
	) {
		log.info("[Configuration][RedisConfig] RedisConnectionFactory redisMessageListenerContainer Created");
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener(messageListenerAdapter, new ChannelTopic(TopicType.EVENT_CHAT_SERVER.getTopic()));
		return container;
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		log.info("[Configuration][RedisConfig] RedisConnectionFactory redisConnectionFactory Created");
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
		return lettuceConnectionFactory;
	}

	@Bean
	public MessageListenerAdapter messageListenerAdapter(MessageListener messageListener) {
		log.info("[Configuration][RedisConfig] MessageListenerAdapter messageListenerAdapter Created");
		return new MessageListenerAdapter(messageListener);
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		log.info("[Configuration][RedisConfig] RedisTemplate redisTemplate Created");
		final RedisTemplate<String, String> template = new RedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		// template.setValueSerializer(new GenericToStringSerializer(Object.class));
		template.setValueSerializer(new Jackson2JsonRedisSerializer<>(EventDto.class));
		return template;
	}

	@Primary
	@Bean
	@ConditionalOnProperty(prefix = "app", name = "publish-event-trigger", havingValue = "true")
	public MessagePublisher realMessagePublisher(RedisTemplate redisTemplate) {
		log.info("[Configuration][RedisConfig] MessagePublisher messagePublisher Created");
		return new RedisMessagePublisher(redisTemplate);
	}

	@Bean
	@ConditionalOnProperty(prefix = "app", name = "publish-event-trigger", havingValue = "false")
	public MessagePublisher fakeMessagePublisher() {
		log.info("[Configuration][RedisConfig] MessagePublisher fakeMessagePublisher Created");
		return new FakeMessagePublisher();
	}
}
