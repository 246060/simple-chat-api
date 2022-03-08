package xyz.jocn.chat.message.repo.room_message_mark;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.message.entity.RoomMessageEntity;
import xyz.jocn.chat.message.entity.RoomMessageMarkEntity;

@Repository
public interface RoomMessageMarkRepository
	extends JpaRepository<RoomMessageMarkEntity, Long>, RoomMessageMarkRepositoryExt {

	List<RoomMessageMarkEntity> findAllByRoomMessage(RoomMessageEntity roomMessage);
}
