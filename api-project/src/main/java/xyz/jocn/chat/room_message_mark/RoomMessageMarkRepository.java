package xyz.jocn.chat.room_message_mark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomMessageMarkRepository extends JpaRepository<PeopleBookEntity, Long> {
}
