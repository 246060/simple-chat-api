package xyz.jocn.chat.user.dto;

import java.util.List;

import lombok.Data;

@Data
public class FriendDeleteRequestDto {
	private Long sourceId;
	private List<Long> targetIds;
}
