package xyz.jocn.chat.auth.dto;

import lombok.Data;

@Data
public class TokenCreateRequestDto {
	private String email;
	private String password;
}
