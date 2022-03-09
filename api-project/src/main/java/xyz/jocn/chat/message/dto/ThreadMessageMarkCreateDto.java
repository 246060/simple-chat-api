package xyz.jocn.chat.message.dto;

import lombok.Data;
import xyz.jocn.chat.message.enums.MessageMarkFlag;

@Data
public class ThreadMessageMarkCreateDto {
	private Long userId;

	private Long threadMessageId;
	private MessageMarkFlag type;
}
