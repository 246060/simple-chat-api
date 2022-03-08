package xyz.jocn.chat.chat_space.exception;

public class NotFoundRoomException extends RuntimeException{
	public NotFoundRoomException() {
	}

	public NotFoundRoomException(String message) {
		super(message);
	}

	public NotFoundRoomException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundRoomException(Throwable cause) {
		super(cause);
	}
}
