package xyz.jocn.chat.message.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MessageTextSendRequestDto {
	private String message;
	private Long parentId;
	private List<Long> mentions;
	// private List<Long>  mentionedUsers;
}
