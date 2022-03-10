package xyz.jocn.chat.user.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FriendGroupConverter {
	FriendGroupConverter INSTANCE = Mappers.getMapper(FriendGroupConverter.class);
}
