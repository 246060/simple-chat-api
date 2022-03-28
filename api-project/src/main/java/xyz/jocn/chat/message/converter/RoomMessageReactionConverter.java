package xyz.jocn.chat.message.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.message.dto.RoomMessageReactionDto;
import xyz.jocn.chat.message.entity.RoomMessageReactionEntity;

@Mapper
public interface RoomMessageReactionConverter {

	RoomMessageReactionConverter INSTANCE = Mappers.getMapper(RoomMessageReactionConverter.class);

	RoomMessageReactionDto toDto(RoomMessageReactionEntity entity);

	List<RoomMessageReactionDto> toDto(List<RoomMessageReactionEntity> entities);
}
