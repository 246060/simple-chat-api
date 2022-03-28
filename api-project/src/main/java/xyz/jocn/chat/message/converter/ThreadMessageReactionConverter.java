package xyz.jocn.chat.message.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.message.dto.ThreadMessageReactionDto;
import xyz.jocn.chat.message.entity.ThreadMessageReactionEntity;

@Mapper
public interface ThreadMessageReactionConverter {

	ThreadMessageReactionConverter INSTANCE = Mappers.getMapper(ThreadMessageReactionConverter.class);

	ThreadMessageReactionDto toDto(ThreadMessageReactionEntity entity);

	List<ThreadMessageReactionDto> toDto(List<ThreadMessageReactionEntity> entity);
}
