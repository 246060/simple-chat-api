package xyz.jocn.chat.message.repo;

import java.util.List;

import xyz.jocn.chat.common.dto.SliceCriteria;
import xyz.jocn.chat.message.MessageEntity;
import xyz.jocn.chat.message.dto.MessageDto;

public interface MessageRepositoryExt {

	List<MessageEntity> findMessagesInChannel(long channelId, SliceCriteria<Long> criteria);

	List<MessageDto> findMessageDtosInChannel(long channelId, SliceCriteria<Long> criteria);

	Long findLastMessageIdInChannel(long channelId);
}
