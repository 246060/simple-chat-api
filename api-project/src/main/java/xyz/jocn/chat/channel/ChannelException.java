package xyz.jocn.chat.channel;

public class ChannelException extends RuntimeException{

	public ChannelException() {
	}

	public ChannelException(String message) {
		super(message);
	}

	public ChannelException(String message, Throwable cause) {
		super(message, cause);
	}

	public ChannelException(Throwable cause) {
		super(cause);
	}
}
