package xyz.jocn.chat.chat_space.exception;

public class RoomException extends RuntimeException{

	public RoomException() {
	}

	public RoomException(String message) {
		super(message);
	}

	public RoomException(String message, Throwable cause) {
		super(message, cause);
	}

	public RoomException(Throwable cause) {
		super(cause);
	}
}
