package xyz.jocn.chat.user;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.user.dto.UserDto;
import xyz.jocn.chat.user.entity.UserEntity;

@Mapper
public interface UserConverter {

	UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

	@Named(value = "toDto")
	UserDto toDto(UserEntity entity);

	@IterableMapping(qualifiedByName = "toDto")
	List<UserDto> toDto(List<UserEntity> entities);

	default UserDto toSummaryDto(UserEntity entity) {
		UserDto userDto = new UserDto();
		userDto.setId(entity.getId());
		userDto.setName(entity.getName());
		userDto.setProfileImgUrl(entity.getProfileImgUrl());
		return userDto;
	}
}
