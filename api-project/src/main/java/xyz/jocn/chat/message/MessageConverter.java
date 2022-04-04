package xyz.jocn.chat.message;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.message.dto.MessageDto;

@Mapper
public interface MessageConverter {

	MessageConverter INSTANCE = Mappers.getMapper(MessageConverter.class);

	MessageDto toDto(MessageEntity entity);

	List<MessageDto> toDto(List<MessageEntity> entities);
}
