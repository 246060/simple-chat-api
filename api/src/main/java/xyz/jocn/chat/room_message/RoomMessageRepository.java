package xyz.jocn.chat.room_message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomMessageRepository extends JpaRepository<RoomMessageEntity,Long> {
}
