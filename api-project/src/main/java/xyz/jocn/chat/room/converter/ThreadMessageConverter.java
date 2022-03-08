package xyz.jocn.chat.room.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.room.dto.ThreadMessageDto;
import xyz.jocn.chat.room.entity.ThreadMessageEntity;

@Mapper
public interface ThreadMessageConverter {
	ThreadMessageConverter INSTANCE = Mappers.getMapper(ThreadMessageConverter.class);

	ThreadMessageDto toDto(ThreadMessageEntity entity);

	List<ThreadMessageDto> toDto(List<ThreadMessageEntity> entity);
}
