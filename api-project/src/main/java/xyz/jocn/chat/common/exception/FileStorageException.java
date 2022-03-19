package xyz.jocn.chat.common.exception;

public class FileStorageException extends RuntimeException{
	public FileStorageException() {
	}

	public FileStorageException(String message) {
		super(message);
	}

	public FileStorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileStorageException(Throwable cause) {
		super(cause);
	}
}
