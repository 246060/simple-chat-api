package xyz.jocn.chat.thread.dto;

import lombok.Data;

@Data
public class ThreadDto {
	private Long threadId;
	private Long roomId;
	private Long roomMessageId;
}
