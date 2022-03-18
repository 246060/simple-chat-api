package xyz.jocn.chat.user.repo.friend;

import java.util.List;

import xyz.jocn.chat.user.dto.FriendSearchDto;
import xyz.jocn.chat.user.entity.FriendEntity;

public interface FriendRepositoryExt {

	List<FriendEntity> findAllBySourceIdAndCondition(Long sourceId, FriendSearchDto condition);
}
