package xyz.jocn.chat.participant.dto;

import java.util.List;

import lombok.Data;

@Data
public class RoomInviteRequestDto {
	private List<Long> invitees;
}
