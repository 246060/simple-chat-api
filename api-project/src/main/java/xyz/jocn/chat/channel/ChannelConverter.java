package xyz.jocn.chat.channel;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import xyz.jocn.chat.channel.dto.ChannelDto;
import xyz.jocn.chat.participant.ParticipantConverter;

@Mapper(uses = {ParticipantConverter.class})
public interface ChannelConverter {

	ChannelConverter INSTANCE = Mappers.getMapper(ChannelConverter.class);

	// @Mapping(target = "channelId", source = "id")
	ChannelDto toDto(ChannelEntity entity);

	List<ChannelDto> toDto(List<ChannelEntity> entities);
}
