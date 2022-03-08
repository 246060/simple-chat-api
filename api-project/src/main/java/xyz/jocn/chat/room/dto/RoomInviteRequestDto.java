package xyz.jocn.chat.room.dto;

import java.util.List;

import lombok.Data;

@Data
public class RoomInviteRequestDto {
	private Long roomId;
	private List<Long> invitees;
}
