package xyz.jocn.chat.message.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.message.dto.RoomMessageMarkDto;
import xyz.jocn.chat.message.entity.RoomMessageMarkEntity;

@Mapper
public interface RoomMessageMarkConverter {

	RoomMessageMarkConverter INSTANCE = Mappers.getMapper(RoomMessageMarkConverter.class);

	RoomMessageMarkDto toDto(RoomMessageMarkEntity entity);

	List<RoomMessageMarkDto> toDto(List<RoomMessageMarkEntity> entities);
}
