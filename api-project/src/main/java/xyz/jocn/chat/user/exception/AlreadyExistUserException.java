package xyz.jocn.chat.user.exception;


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
