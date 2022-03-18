package xyz.jocn.chat.user.dto;

import java.util.List;

import lombok.Data;

@Data
public class FriendBlockCreateRequestDto {
	private Long sourceId;
	private Long targetId;
}
