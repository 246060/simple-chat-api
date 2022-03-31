package xyz.jocn.chat.channel.repo;

import java.util.List;
import java.util.Optional;

import xyz.jocn.chat.channel.dto.ChannelDto;

public interface ChannelRepositoryExt {
	List<ChannelDto> findAllMyChannels(long uid);

	Optional<ChannelDto> findMyChannelById(long id);
}
