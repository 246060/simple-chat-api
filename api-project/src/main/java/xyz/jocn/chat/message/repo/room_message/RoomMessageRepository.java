package xyz.jocn.chat.message.repo.room_message;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.message.entity.RoomMessageEntity;

@Repository
public interface RoomMessageRepository extends JpaRepository<RoomMessageEntity, Long>, RoomMessageRepositoryExt {

	Page<RoomMessageEntity> findAllByRoomId(long roomId, Pageable pageable);
	Optional<RoomMessageEntity> findByIdAndSenderId(long messageId, long roomParticipant);
}
