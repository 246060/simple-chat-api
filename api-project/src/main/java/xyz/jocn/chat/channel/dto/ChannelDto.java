package xyz.jocn.chat.channel.dto;

import java.util.List;

import lombok.Data;
import xyz.jocn.chat.participant.dto.ParticipantDto;

@Data
public class ChannelDto {
	private Long id;
	private List<ParticipantDto> participants;
}
