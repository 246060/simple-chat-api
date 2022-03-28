package xyz.jocn.chat.participant.dto;

import lombok.Data;
import xyz.jocn.chat.user.dto.UserDto;

@Data
public class ParticipantDto {
	private Long id;
	private UserDto user;
}
