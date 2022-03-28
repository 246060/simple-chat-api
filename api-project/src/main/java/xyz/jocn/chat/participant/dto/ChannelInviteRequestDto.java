package xyz.jocn.chat.participant.dto;

import java.util.List;

import lombok.Data;

@Data
public class ChannelInviteRequestDto {
	private List<Long> invitees;
}
