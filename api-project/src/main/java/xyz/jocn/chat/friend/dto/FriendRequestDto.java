package xyz.jocn.chat.friend.dto;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class FriendRequestDto {

	@Email
	private String targetEmail;
}
