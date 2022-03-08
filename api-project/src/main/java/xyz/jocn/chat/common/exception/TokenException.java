package xyz.jocn.chat.common.exception;

public class TokenException extends RuntimeException {

	private TokenErrorCode tokenErrorCode;

	public TokenErrorCode getTokenErrorCode() {
		return tokenErrorCode;
	}

	public TokenException() {
	}

	public TokenException(TokenErrorCode tokenErrorCode) {
		this.tokenErrorCode = tokenErrorCode;
	}

	public TokenException(String message, TokenErrorCode tokenErrorCode) {
		super(message);
		this.tokenErrorCode = tokenErrorCode;
	}

	public TokenException(String message, Throwable cause, TokenErrorCode tokenErrorCode) {
		super(message, cause);
		this.tokenErrorCode = tokenErrorCode;
	}

	public TokenException(Throwable cause, TokenErrorCode tokenErrorCode) {
		super(cause);
		this.tokenErrorCode = tokenErrorCode;
	}
}
