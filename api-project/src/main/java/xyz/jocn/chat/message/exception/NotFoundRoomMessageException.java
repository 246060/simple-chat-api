package xyz.jocn.chat.message.exception;

public class NotFoundRoomMessageException extends RuntimeException{
	public NotFoundRoomMessageException() {
	}

	public NotFoundRoomMessageException(String message) {
		super(message);
	}

	public NotFoundRoomMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundRoomMessageException(Throwable cause) {
		super(cause);
	}
}
