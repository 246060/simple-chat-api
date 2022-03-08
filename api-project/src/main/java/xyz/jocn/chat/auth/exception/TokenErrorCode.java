package xyz.jocn.chat.auth.exception;

public enum TokenErrorCode {
	WhenGenerateAccessToken,
	WhenVerifyAccessToken,
	WhenGetClaim,
	NotExistRefreshToken,
	WhenGenerateKey
}
