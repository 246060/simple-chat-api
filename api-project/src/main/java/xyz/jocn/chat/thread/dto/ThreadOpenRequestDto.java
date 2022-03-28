package xyz.jocn.chat.thread.dto;

import lombok.Data;

@Data
public class ThreadOpenRequestDto {
	private Long roomId;
	private Long messageId;
	private Long roomMessageId;
	private Long userId;
}
