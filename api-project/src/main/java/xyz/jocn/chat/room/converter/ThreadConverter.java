package xyz.jocn.chat.room.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.room.dto.ThreadDto;
import xyz.jocn.chat.room.entity.ThreadEntity;

@Mapper
public interface ThreadConverter {

	ThreadConverter INSTANCE = Mappers.getMapper(ThreadConverter.class);

	ThreadDto toDto(ThreadEntity entity);
}
