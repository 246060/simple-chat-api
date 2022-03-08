package xyz.jocn.chat.message.dto;

import lombok.Data;
import xyz.jocn.chat.message.enums.MessageMarkFlag;

@Data
public class RoomMessageMarkCreateDto {
	private Long userId;
	private MessageMarkFlag type;
	private Long participantId;
	private Long messageId;
}
