package xyz.jocn.chat.common.pubsub;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChatRedisProducer implements ChatProducer{

	@Override
	public boolean emit(PublishEvent producerEvent) {
		return false;
	}
}
