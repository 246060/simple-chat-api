package xyz.jocn.chat.participant.dto;

import lombok.Data;

@Data
public class ChannelExitDto {
	private Long channelId;
	private Long userId;
	private Long participantId;
}
