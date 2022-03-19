package xyz.jocn.chat.friend.repo.friend;

import java.util.List;

import xyz.jocn.chat.friend.dto.FriendSearchDto;
import xyz.jocn.chat.friend.entity.FriendEntity;

public interface FriendRepositoryExt {

	List<FriendEntity> findAllBySourceIdAndCondition(Long sourceId, FriendSearchDto condition);
}
