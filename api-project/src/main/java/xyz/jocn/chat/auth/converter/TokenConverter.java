package xyz.jocn.chat.auth.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TokenConverter {

	TokenConverter INSTANCE = Mappers.getMapper(TokenConverter.class);
}
