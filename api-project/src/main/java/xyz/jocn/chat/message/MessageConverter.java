package xyz.jocn.chat.message;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.message.dto.MessageDto;
import xyz.jocn.chat.message.entity.MessageEntity;

@Mapper
public interface MessageConverter {

	MessageConverter INSTANCE = Mappers.getMapper(MessageConverter.class);

	MessageDto toDto(MessageEntity entity);
}
