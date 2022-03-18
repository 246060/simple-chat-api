package xyz.jocn.chat.user.dto;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class FriendRequestDto {

	@Email
	private String targetEmail;
	private Long targetUid;
}
