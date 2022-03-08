package xyz.jocn.chat.common.exception;


public class AlreadyExistUserException extends RuntimeException{

	public AlreadyExistUserException() {
	}

	public AlreadyExistUserException(String message) {
		super(message);
	}

	public AlreadyExistUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlreadyExistUserException(Throwable cause) {
		super(cause);
	}
}
