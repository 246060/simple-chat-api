package xyz.jocn.chat.room.dto;

import lombok.Data;

@Data
public class RoomParticipantDto {
	private Long id;
	private Long userId;
	private String name;
}
