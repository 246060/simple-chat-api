package xyz.jocn.chat.message.exception;

public class NotAvailableMessageTypeException extends RuntimeException{
	public NotAvailableMessageTypeException() {
	}

	public NotAvailableMessageTypeException(String message) {
		super(message);
	}

	public NotAvailableMessageTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAvailableMessageTypeException(Throwable cause) {
		super(cause);
	}
}
