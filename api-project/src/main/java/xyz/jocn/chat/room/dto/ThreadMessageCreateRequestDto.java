package xyz.jocn.chat.room.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import xyz.jocn.chat.room.enums.ChatMessageType;

@Data
public class ThreadMessageCreateRequestDto {
	private Long threadParticipantId;
	private ChatMessageType type;
	private List<MultipartFile> files;
	private String message;
}
