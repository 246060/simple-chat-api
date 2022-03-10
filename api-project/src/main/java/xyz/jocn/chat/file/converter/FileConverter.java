package xyz.jocn.chat.file.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.auth.TokenConverter;

@Mapper
public interface FileConverter {
	FileConverter INSTANCE = Mappers.getMapper(FileConverter.class);
}
