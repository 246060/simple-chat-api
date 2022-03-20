package xyz.jocn.chat.participant.dto;

import lombok.Data;

@Data
public class RoomExitRequestDto {
	private Long userId;
	private Long participantId;
}
