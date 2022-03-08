package xyz.jocn.chat.pubsub;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChatRedisProducer implements ChatProducer{

	@Override
	public boolean emit(ProducerEvent producerEvent) {
		return false;
	}
}
