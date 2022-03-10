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

	@Mapping(target = "threadId", source = "id")
	@Mapping(target = "roomId", source = "room.id")
	@Mapping(target = "roomMessageId", source = "roomMessage.id")
	ThreadDto toDto(ThreadEntity entity);

	@Mapping(target = "threadId", source = "thread.id")
	@Mapping(target = "roomId", source = "thread.room.id")
	@Mapping(target = "roomMessageId", source = "thread.roomMessage.id")
	ThreadDto toDto(ThreadParticipantEntity threadParticipantEntity);
}
