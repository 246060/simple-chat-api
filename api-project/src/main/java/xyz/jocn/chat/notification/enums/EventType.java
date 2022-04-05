package xyz.jocn.chat.notification.enums;

public enum EventType {

	channel_join("channel.participant.join"),
	channel_exit("channel.participant.exit"),

	channel_message_new("channel.message.new"),
	channel_message_deleted("channel.message.deleted"),
	channel_message_reaction("channel.message.reaction"),
	channel_message_read("channel.message.read"),

	all_ad("all.ad"),
	all_urgent("all.urgent");

	private String eventType;

	EventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventType() {
		return eventType;
	}
}
