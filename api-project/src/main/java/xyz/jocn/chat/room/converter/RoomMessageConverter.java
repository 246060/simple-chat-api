package xyz.jocn.chat.room.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.room.dto.RoomMessageDto;
import xyz.jocn.chat.room.entity.RoomMessageEntity;

@Mapper
public interface RoomMessageConverter {

	RoomMessageConverter INSTANCE = Mappers.getMapper(RoomMessageConverter.class);

	RoomMessageDto toDto(RoomMessageEntity entity);
}
