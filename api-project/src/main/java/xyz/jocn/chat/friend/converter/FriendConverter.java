package xyz.jocn.chat.friend.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.friend.dto.FriendDto;
import xyz.jocn.chat.friend.entity.FriendEntity;

@Mapper
public interface FriendConverter {
	FriendConverter INSTANCE = Mappers.getMapper(FriendConverter.class);

	FriendDto toDto(FriendEntity entities);
	List<FriendDto> toDto(List<FriendEntity> entities);
}
