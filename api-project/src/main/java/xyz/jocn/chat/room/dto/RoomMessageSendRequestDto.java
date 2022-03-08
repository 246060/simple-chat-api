package xyz.jocn.chat.room.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import xyz.jocn.chat.room.enums.ChatMessageType;

@Data
public class RoomMessageSendRequestDto {
	private Long userId;
	private ChatMessageType type;
	private Long participantId;
	private Long roomId;
	private List<MultipartFile> files;
	private String message;
}
