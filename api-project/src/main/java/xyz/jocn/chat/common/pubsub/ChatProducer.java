package xyz.jocn.chat.common.pubsub;

public interface ChatProducer {

	boolean emit(ProducerEvent producerEvent);
}
