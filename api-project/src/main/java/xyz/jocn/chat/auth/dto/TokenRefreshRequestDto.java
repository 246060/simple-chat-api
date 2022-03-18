package xyz.jocn.chat.auth.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TokenRefreshRequestDto {

	@NotEmpty
	@JsonProperty("refresh_token")
	private String refreshToken;
}
