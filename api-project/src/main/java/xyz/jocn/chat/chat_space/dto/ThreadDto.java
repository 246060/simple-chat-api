package xyz.jocn.chat.chat_space.dto;

import lombok.Data;

@Data
public class ThreadDto {
	private Long threadId;
	private Long roomId;
	private Long roomMessageId;
}
