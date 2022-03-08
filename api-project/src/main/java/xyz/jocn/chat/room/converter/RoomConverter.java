package xyz.jocn.chat.room.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.room.dto.RoomDto;
import xyz.jocn.chat.room.entity.RoomEntity;
import xyz.jocn.chat.room.entity.RoomParticipantEntity;

@Mapper(uses = {RoomParticipantConverter.class})
public interface RoomConverter {

	RoomConverter INSTANCE = Mappers.getMapper(RoomConverter.class);

	@Mapping(source = "id", target = "roomId")
	RoomDto toDto(RoomEntity entity);
}
