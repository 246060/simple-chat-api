package xyz.jocn.chat.room.dto;

import lombok.Data;
import xyz.jocn.chat.room.enums.MessageMarkFlag;

@Data
public class ThreadMessageMarkCreateRequestDto {
	private MessageMarkFlag type;
	private Long threadParticipantId;
	private Long threadMessageId;
}
