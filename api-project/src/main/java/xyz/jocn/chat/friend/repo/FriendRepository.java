package xyz.jocn.chat.friend.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.friend.FriendEntity;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, Long>, FriendRepositoryExt {
	List<FriendEntity> findAllBySourceId(Long sourceId);

	Optional<FriendEntity> findByIdAndSourceId(Long id, Long sourceId);

	void deleteAllBySourceId(Long sourceId);

	void deleteAllBySourceIdAndTargetIdIn(Long sourceId, List<Long> targetIds);
}
