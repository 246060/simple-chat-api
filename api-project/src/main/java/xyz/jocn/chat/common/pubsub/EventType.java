package xyz.jocn.chat.common.pubsub;

public enum EventType {
	ROOM_OPEN("room.open"),
	THREAD_OPEN("thread.open");

	private String message;

	EventType(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
