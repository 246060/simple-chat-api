package xyz.jocn.chat.room.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.room.dto.ThreadParticipantDto;
import xyz.jocn.chat.room.entity.ThreadParticipantEntity;

@Mapper
public interface ThreadParticipantConverter {
	ThreadParticipantConverter INSTANCE = Mappers.getMapper(ThreadParticipantConverter.class);

	ThreadParticipantDto toDto(ThreadParticipantEntity entity);
}
