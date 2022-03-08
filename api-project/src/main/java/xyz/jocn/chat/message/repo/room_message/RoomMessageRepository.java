package xyz.jocn.chat.message.repo.room_message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.message.entity.RoomMessageEntity;

@Repository
public interface RoomMessageRepository extends JpaRepository<RoomMessageEntity, Long>, RoomMessageRepositoryExt {
}
