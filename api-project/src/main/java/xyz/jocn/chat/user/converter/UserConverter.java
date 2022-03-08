package xyz.jocn.chat.user.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.user.dto.UserDto;
import xyz.jocn.chat.user.entity.UserEntity;

@Mapper
public interface UserConverter {

	UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

	UserDto toDto(UserEntity entity);
}
