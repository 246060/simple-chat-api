package xyz.jocn.chat.participant.dto;

import lombok.Data;

@Data
public class ThreadJoinDto {
	private Long roomId;
	private Long threadId;
	private Long userId;
}
