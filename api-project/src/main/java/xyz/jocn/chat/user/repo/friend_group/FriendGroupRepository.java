package xyz.jocn.chat.user.repo.friend_group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.user.entity.FriendGroupEntity;

@Repository
public interface FriendGroupRepository extends JpaRepository<FriendGroupEntity, Long>, FriendGroupRepositoryExt {
}
