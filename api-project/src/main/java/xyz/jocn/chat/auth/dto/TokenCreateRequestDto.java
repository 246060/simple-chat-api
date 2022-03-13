package xyz.jocn.chat.auth.dto;

import javax.validation.constraints.Email;

import lombok.Data;
import xyz.jocn.chat.common.validation.UserPassword;

@Data
public class TokenCreateRequestDto {

	@Email
	private String email;

	@UserPassword
	private String password;
}
