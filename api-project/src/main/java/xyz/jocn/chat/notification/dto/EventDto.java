package xyz.jocn.chat.notification.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import xyz.jocn.chat.message.enums.MessageType;
import xyz.jocn.chat.user.entity.UserEntity;
import xyz.jocn.chat.user.dto.UserDto;

@Getter
@Builder
public class EventDto {
	private Long channelId;
	private Long messageId;
	private MessageType messageType;
	private List<Long> mentions;
	private List<UserEntity> invitees;
	private UserDto user;
}
