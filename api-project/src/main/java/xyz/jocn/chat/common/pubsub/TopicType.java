package xyz.jocn.chat.common.pubsub;

public enum TopicType {

	EVENT_CHAT_SERVER("event.chat.server"),
	EVENT_API_SERVER("event.api.server");

	private String topic;

	TopicType(String topic) {
		this.topic = topic;
	}

	public String getTopic() {
		return topic;
	}
}
