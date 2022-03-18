package xyz.jocn.chat.user.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.auth.TokenConverter;
import xyz.jocn.chat.user.dto.FriendBlockDto;
import xyz.jocn.chat.user.entity.FriendBlockEntity;

@Mapper
public interface FriendBlockConverter {
	FriendBlockConverter INSTANCE = Mappers.getMapper(FriendBlockConverter.class);

	List<FriendBlockDto> toDto(List<FriendBlockEntity> entities);
}
