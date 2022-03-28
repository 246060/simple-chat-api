package xyz.jocn.chat.participant.dto;

import lombok.Data;

@Data
public class RoomExitDto {
	private Long roomId;
	private Long userId;
	private Long participantId;
}
