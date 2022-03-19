package xyz.jocn.chat.room;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.room.dto.RoomDto;

@Mapper
	// (uses = {RoomParticipantConverter.class})
public interface RoomConverter {

	RoomConverter INSTANCE = Mappers.getMapper(RoomConverter.class);

	@Mapping(target = "roomId", source = "id")
	RoomDto toDto(RoomEntity entity);
}
