package xyz.jocn.chat.message.dto;

import lombok.Data;
import xyz.jocn.chat.message.enums.MessageMarkFlag;

@Data
public class ThreadMessageMarkCreateDto {
	private MessageMarkFlag type;
	private Long threadParticipantId;
	private Long threadMessageId;
}
