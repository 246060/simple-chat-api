package xyz.jocn.chat.message.repo.room_message_file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.message.entity.RoomMessageFileEntity;

@Repository
public interface RoomMessageFileRepository extends JpaRepository<RoomMessageFileEntity, Long>,RoomMessageFileRepositoryExt {
}
