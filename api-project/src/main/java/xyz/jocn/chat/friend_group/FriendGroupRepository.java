package xyz.jocn.chat.friend_group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendGroupRepository extends JpaRepository<PeopleBookEntity, Long> {
}
