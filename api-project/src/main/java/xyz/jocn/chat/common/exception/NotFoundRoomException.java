package xyz.jocn.chat.common.exception;

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
