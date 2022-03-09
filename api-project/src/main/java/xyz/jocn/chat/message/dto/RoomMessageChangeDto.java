package xyz.jocn.chat.message.dto;

import lombok.Data;
import xyz.jocn.chat.message.enums.RoomMessageState;

@Data
public class RoomMessageChangeDto {
	private Long messageId;
	private RoomMessageState state;
}
