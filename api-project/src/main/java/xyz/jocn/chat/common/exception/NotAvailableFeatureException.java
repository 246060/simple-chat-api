package xyz.jocn.chat.common.exception;

public class NotAvailableFeatureException extends RuntimeException{
	public NotAvailableFeatureException() {
	}

	public NotAvailableFeatureException(String message) {
		super(message);
	}

	public NotAvailableFeatureException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAvailableFeatureException(Throwable cause) {
		super(cause);
	}
}
