package xyz.jocn.chat.chat_space.exception;

public class NotFoundThreadException extends RuntimeException {
	public NotFoundThreadException() {
	}

	public NotFoundThreadException(String message) {
		super(message);
	}

	public NotFoundThreadException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundThreadException(Throwable cause) {
		super(cause);
	}
}
