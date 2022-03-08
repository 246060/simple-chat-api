package xyz.jocn.chat.participant.dto;

import lombok.Data;

@Data
public class RoomParticipantDto {
	private Long id;
	private Long userId;
	private String name;
}
