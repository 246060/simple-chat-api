package xyz.jocn.chat.reaction;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.reaction.dto.ReactionDto;

@Mapper
public interface ReactionConverter {

	ReactionConverter INSTANCE = Mappers.getMapper(ReactionConverter.class);

	ReactionDto toDto(ReactionEntity entity);

	List<ReactionDto> toDto(List<ReactionEntity> entities);
}
