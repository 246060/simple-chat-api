package xyz.jocn.chat.pubsub;

public interface ChatProducer {

	boolean emit(ProducerEvent producerEvent);
}
