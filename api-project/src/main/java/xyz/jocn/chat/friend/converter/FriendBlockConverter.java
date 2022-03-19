package xyz.jocn.chat.friend.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.friend.dto.FriendBlockDto;
import xyz.jocn.chat.friend.entity.FriendBlockEntity;

@Mapper
public interface FriendBlockConverter {
	FriendBlockConverter INSTANCE = Mappers.getMapper(FriendBlockConverter.class);

	List<FriendBlockDto> toDto(List<FriendBlockEntity> entities);
}
