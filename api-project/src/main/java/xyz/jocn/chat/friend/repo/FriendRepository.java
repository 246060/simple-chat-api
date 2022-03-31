package xyz.jocn.chat.friend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.friend.FriendEntity;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, Long>, FriendRepositoryExt {

	Optional<FriendEntity> findByIdAndSourceId(long id, long sourceId);

	void deleteAllBySourceId(long sourceId);
}
