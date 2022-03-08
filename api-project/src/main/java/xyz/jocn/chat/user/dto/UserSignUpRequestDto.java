package xyz.jocn.chat.user.dto;

import lombok.Data;

@Data
public class UserSignUpRequestDto {
	private String email;
	private String name;
	private String password;
}
