package xyz.jocn.chat.chat_space.dto;

import lombok.Data;

@Data
public class ThreadOpenRequestDto {
	private Long participantId;
	private Long messageId;
}
