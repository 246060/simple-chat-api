package xyz.jocn.chat.message.exception;

public class NotFoundThreadMessageException extends RuntimeException{
	public NotFoundThreadMessageException() {
	}

	public NotFoundThreadMessageException(String message) {
		super(message);
	}

	public NotFoundThreadMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundThreadMessageException(Throwable cause) {
		super(cause);
	}
}
