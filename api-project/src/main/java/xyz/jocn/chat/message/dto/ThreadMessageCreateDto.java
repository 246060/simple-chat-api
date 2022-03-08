package xyz.jocn.chat.message.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import xyz.jocn.chat.message.enums.ChatMessageType;

@Data
public class ThreadMessageCreateDto {
	private Long threadParticipantId;
	private ChatMessageType type;
	private List<MultipartFile> files;
	private String message;
}
