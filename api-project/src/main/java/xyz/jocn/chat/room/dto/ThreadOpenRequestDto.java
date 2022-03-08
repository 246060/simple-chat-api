package xyz.jocn.chat.room.dto;

import lombok.Data;

@Data
public class ThreadOpenRequestDto {
	private Long participantId;
	private Long messageId;
}
