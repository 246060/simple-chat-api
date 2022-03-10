package xyz.jocn.chat.chat_space.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.chat_space.dto.RoomDto;
import xyz.jocn.chat.chat_space.entity.RoomEntity;
import xyz.jocn.chat.participant.converter.RoomParticipantConverter;

@Mapper
	// (uses = {RoomParticipantConverter.class})
public interface RoomConverter {

	RoomConverter INSTANCE = Mappers.getMapper(RoomConverter.class);

	@Mapping(target = "roomId", source = "id")
	RoomDto toDto(RoomEntity entity);
}
