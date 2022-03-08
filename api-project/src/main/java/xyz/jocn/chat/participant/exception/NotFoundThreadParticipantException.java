package xyz.jocn.chat.participant.exception;

public class NotFoundThreadParticipantException extends RuntimeException{
	public NotFoundThreadParticipantException() {
	}

	public NotFoundThreadParticipantException(String message) {
		super(message);
	}

	public NotFoundThreadParticipantException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundThreadParticipantException(Throwable cause) {
		super(cause);
	}
}
