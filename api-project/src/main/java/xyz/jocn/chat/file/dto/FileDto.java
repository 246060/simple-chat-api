package xyz.jocn.chat.file.dto;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class FileDto {
	private Long id;
	private String originName;
	private String contentType;
	private String path;

	@JsonIgnore
	private Resource resource;
}
