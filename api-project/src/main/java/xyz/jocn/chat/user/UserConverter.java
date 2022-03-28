package xyz.jocn.chat.user;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.user.dto.UserDto;

@Mapper
public interface UserConverter {

	UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

	UserDto toDto(UserEntity entity);

	List<UserDto> toDto(List<UserEntity> entities);
}
