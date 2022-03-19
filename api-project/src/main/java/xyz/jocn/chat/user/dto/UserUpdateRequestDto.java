package xyz.jocn.chat.user.dto;

import lombok.Data;

@Data
public class UserUpdateRequestDto {
	private String name;
	private String stateMessage;
}
