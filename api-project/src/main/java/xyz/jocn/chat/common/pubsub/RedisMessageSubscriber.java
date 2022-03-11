package xyz.jocn.chat.common.pubsub;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisMessageSubscriber implements MessageListener {

	private final ObjectMapper objectMapper;
	private final RedisTemplate redisTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		// String messageStr = (String)redisTemplate.getStringSerializer().deserialize(message.getBody());
		// String data = objectMapper.readValue(message, String.class);
		log.info("received message : {}", message.toString());
	}
}
