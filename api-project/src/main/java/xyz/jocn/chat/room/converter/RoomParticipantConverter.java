package xyz.jocn.chat.room.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.room.dto.RoomParticipantDto;
import xyz.jocn.chat.room.entity.RoomParticipantEntity;

@Mapper
public interface RoomParticipantConverter {

	RoomParticipantConverter INSTANCE = Mappers.getMapper(RoomParticipantConverter.class);

	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "name", source = "user.name")
	@Mapping(target = "id", ignore = true)
	RoomParticipantDto toDto(RoomParticipantEntity entity);

	List<RoomParticipantDto> toDto(List<RoomParticipantEntity> entities);
}
