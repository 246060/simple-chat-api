package xyz.jocn.chat.user.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.auth.TokenConverter;

@Mapper
public interface FriendBlockConverter {
	FriendBlockConverter INSTANCE = Mappers.getMapper(FriendBlockConverter.class);
}
