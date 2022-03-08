package xyz.jocn.chat.room.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.room.dto.ThreadMessageMarkDto;
import xyz.jocn.chat.room.entity.ThreadMessageMarkEntity;

@Mapper
public interface ThreadMessageMarkConverter {

	ThreadMessageMarkConverter INSTANCE = Mappers.getMapper(ThreadMessageMarkConverter.class);

	ThreadMessageMarkDto toDto(ThreadMessageMarkEntity entity);
	List<ThreadMessageMarkDto> toDto(List<ThreadMessageMarkEntity> entity);
}
