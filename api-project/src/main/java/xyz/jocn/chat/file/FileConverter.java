package xyz.jocn.chat.file;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.file.dto.FileDto;

@Mapper
public interface FileConverter {
	FileConverter INSTANCE = Mappers.getMapper(FileConverter.class);

	FileDto toBaseDto(FileEntity entity);

	default FileDto toDto(FileEntity entity) {
		FileDto fileDto = toBaseDto(entity);
		fileDto.setPath(String.format("/files/%d/data", entity.getId()));
		return fileDto;
	}
}
