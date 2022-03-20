package xyz.jocn.chat.room.dto;

import java.util.List;

import lombok.Data;
import xyz.jocn.chat.participant.dto.RoomParticipantDto;

@Data
public class RoomDto {
	private Long roomId;
	private List<RoomParticipantDto> participants;
}
