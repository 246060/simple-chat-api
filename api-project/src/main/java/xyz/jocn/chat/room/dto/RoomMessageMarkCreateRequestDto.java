package xyz.jocn.chat.room.dto;

import lombok.Data;
import xyz.jocn.chat.room.enums.MessageMarkFlag;

@Data
public class RoomMessageMarkCreateRequestDto {
	private Long userId;
	private MessageMarkFlag type;
	private Long participantId;
	private Long messageId;
}
