package xyz.jocn.chat.friend.repo;

import java.util.List;

import xyz.jocn.chat.friend.dto.FriendSearchDto;
import xyz.jocn.chat.friend.FriendEntity;

public interface FriendRepositoryExt {

	List<FriendEntity> findAllBySourceIdAndCondition(long sourceId, FriendSearchDto condition);
}
