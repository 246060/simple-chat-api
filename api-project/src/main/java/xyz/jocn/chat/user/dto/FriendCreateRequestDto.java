package xyz.jocn.chat.user.dto;

import lombok.Data;

@Data
public class FriendCreateRequestDto {
	private Long userId;
	private Long targetId;
}
