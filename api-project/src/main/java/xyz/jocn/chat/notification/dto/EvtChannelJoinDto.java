package xyz.jocn.chat.notification.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import xyz.jocn.chat.notification.enums.EventType;
import xyz.jocn.chat.user.dto.UserDto;

@Data
public class EvtChannelJoinDto {

	@Setter(AccessLevel.NONE)
	private String eventType = EventType.channel_join.getEventType();

	private Long channelId;
	private List<UserDto> users;

	public EvtChannelJoinDto(Long channelId, List<UserDto> users) {
		this.channelId = channelId;
		this.users = users;
	}
}
