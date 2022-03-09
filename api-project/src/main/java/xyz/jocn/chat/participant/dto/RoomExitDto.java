package xyz.jocn.chat.participant.dto;

import lombok.Data;

@Data
public class RoomExitDto {
	private Long userId;
	private Long roomId;
	private Long participantId;
}
