package xyz.jocn.chat.participant.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.participant.dto.ThreadParticipantDto;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;

@Mapper
public interface ThreadParticipantConverter {
	ThreadParticipantConverter INSTANCE = Mappers.getMapper(ThreadParticipantConverter.class);

	ThreadParticipantDto toDto(ThreadParticipantEntity entity);

	List<ThreadParticipantDto> toDto(List<ThreadParticipantEntity> entities);
}
