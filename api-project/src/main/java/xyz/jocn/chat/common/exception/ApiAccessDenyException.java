package xyz.jocn.chat.common.exception;

public class ApiAccessDenyException extends RuntimeException {
	public ApiAccessDenyException() {
	}

	public ApiAccessDenyException(String message) {
		super(message);
	}

	public ApiAccessDenyException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiAccessDenyException(Throwable cause) {
		super(cause);
	}
}
