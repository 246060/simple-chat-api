package xyz.jocn.chat.message.dto;

import lombok.Data;
import xyz.jocn.chat.message.enums.ThreadMessageState;

@Data
public class ThreadMessageChangeDto {
	private Long threadMessageId;
	private ThreadMessageState state;
}
