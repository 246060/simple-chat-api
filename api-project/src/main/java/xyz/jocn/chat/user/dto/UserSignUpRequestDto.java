package xyz.jocn.chat.user.dto;

import javax.validation.constraints.Email;

import lombok.Data;
import xyz.jocn.chat.common.validation.UserPassword;

@Data
public class UserSignUpRequestDto {

	@Email
	private String email;
	private String name;

	@UserPassword
	private String password;
}
