package xyz.jocn.chat.user.repo.friend_block;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.user.entity.FriendBlockEntity;

@Repository
public interface FriendBlockRepository extends JpaRepository<FriendBlockEntity, Long>, FriendBlockRepositoryExt {
	List<FriendBlockEntity> findAllBySourceId(Long sourceId);

	Optional<FriendBlockEntity> findBySourceIdAndTargetId(Long sourceId, Long targetId);

	void deleteAllBySourceIdAndTargetIdIn(Long sourceId, List<Long> targetIds);

	void deleteAllBySourceId(Long sourceId);
}
