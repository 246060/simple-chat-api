package xyz.jocn.chat.participant;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.participant.dto.ParticipantDto;

@Mapper
public interface ParticipantConverter {

	ParticipantConverter INSTANCE = Mappers.getMapper(ParticipantConverter.class);

	// @Mapping(target = "userId", source = "user.id")
	// @Mapping(target = "name", source = "user.name")
	// @Mapping(target = "id", ignore = true)
	ParticipantDto toDto(ParticipantEntity entity);

	List<ParticipantDto> toDto(List<ParticipantEntity> entities);
}
