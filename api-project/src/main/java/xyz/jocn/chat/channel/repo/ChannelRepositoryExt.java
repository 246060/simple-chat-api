package xyz.jocn.chat.channel.repo;

import java.util.List;

import xyz.jocn.chat.channel.dto.ChannelDto;

public interface ChannelRepositoryExt {
	List<ChannelDto> findAllMyChannels(long uid);

	List<ChannelDto> findMyChannelById(long id, long uid);
}
