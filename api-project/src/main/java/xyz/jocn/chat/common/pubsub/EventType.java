package xyz.jocn.chat.common.pubsub;

public enum EventType {
	ROOM_EVENT("room"),
	ROOM_MESSAGE_EVENT("room.message"),
	THREAD_EVENT("thread"),
	THREAD_MESSAGE_EVENT("thread.message");

	private String message;

	EventType(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
