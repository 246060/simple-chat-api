package xyz.jocn.chat.room_invite_block;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomInviteBlockRepository extends JpaRepository<PeopleBookEntity, Long> {
}
