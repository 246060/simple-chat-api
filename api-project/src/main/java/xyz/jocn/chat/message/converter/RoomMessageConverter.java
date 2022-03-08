package xyz.jocn.chat.message.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.message.dto.RoomMessageDto;
import xyz.jocn.chat.message.entity.RoomMessageEntity;

@Mapper
public interface RoomMessageConverter {

	RoomMessageConverter INSTANCE = Mappers.getMapper(RoomMessageConverter.class);

	RoomMessageDto toDto(RoomMessageEntity entity);
}
