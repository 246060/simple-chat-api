package xyz.jocn.chat.message.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MessageFileSendRequestDto {
	private MultipartFile file;
	private Long parentId;
	private List<Long> mentions;
}
