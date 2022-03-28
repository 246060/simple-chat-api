package xyz.jocn.chat.message.dto;

import lombok.Data;
import xyz.jocn.chat.message.enums.MessageReactionType;

@Data
public class ThreadMessageReactionAddRequestDto {
	private MessageReactionType type;
}
