package xyz.jocn.chat.user.dto;

import java.util.List;

import lombok.Data;

@Data
public class FriendBlockCancelRequestDto {
	private Long sourceId;
	private List<Long> targetIds;
}
