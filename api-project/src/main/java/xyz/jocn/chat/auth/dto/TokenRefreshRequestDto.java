package xyz.jocn.chat.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TokenRefreshRequestDto {
	@JsonProperty("refresh_token")
	private String refreshToken;
}
