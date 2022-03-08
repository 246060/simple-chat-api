package xyz.jocn.chat.participant.exception;

public class NotFoundRoomParticipantException extends RuntimeException{

	public NotFoundRoomParticipantException() {
	}

	public NotFoundRoomParticipantException(String message) {
		super(message);
	}

	public NotFoundRoomParticipantException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundRoomParticipantException(Throwable cause) {
		super(cause);
	}
}
