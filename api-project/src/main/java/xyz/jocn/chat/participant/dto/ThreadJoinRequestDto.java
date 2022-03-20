package xyz.jocn.chat.participant.dto;

import lombok.Data;

@Data
public class ThreadJoinRequestDto {
	private Long threadId;
	private Long userId;
}
