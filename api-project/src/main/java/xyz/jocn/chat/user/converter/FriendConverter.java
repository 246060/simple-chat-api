package xyz.jocn.chat.user.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FriendConverter {
	FriendConverter INSTANCE = Mappers.getMapper(FriendConverter.class);
}
