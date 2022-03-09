package xyz.jocn.chat.chat_space.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.chat_space.dto.ThreadDto;
import xyz.jocn.chat.chat_space.entity.ThreadEntity;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;

@Mapper
public interface ThreadConverter {

	ThreadConverter INSTANCE = Mappers.getMapper(ThreadConverter.class);

	ThreadDto toDto(ThreadEntity entity);

	@Mapping(target = ".", source = "thread")
	ThreadDto toDto(ThreadParticipantEntity threadParticipantEntity);
}
