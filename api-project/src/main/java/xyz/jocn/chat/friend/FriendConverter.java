package xyz.jocn.chat.friend;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.friend.dto.FriendDto;

@Mapper
public interface FriendConverter {
	FriendConverter INSTANCE = Mappers.getMapper(FriendConverter.class);

	FriendDto toDto(FriendEntity entities);
	List<FriendDto> toDto(List<FriendEntity> entities);
}
