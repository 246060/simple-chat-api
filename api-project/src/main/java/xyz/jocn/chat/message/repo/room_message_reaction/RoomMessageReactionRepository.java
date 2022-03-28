package xyz.jocn.chat.message.repo.room_message_reaction;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.message.entity.RoomMessageEntity;
import xyz.jocn.chat.message.entity.RoomMessageReactionEntity;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;

@Repository
public interface RoomMessageReactionRepository
	extends JpaRepository<RoomMessageReactionEntity, Long>, RoomMessageReactionRepositoryExt {

	List<RoomMessageReactionEntity> findAllByRoomMessage(RoomMessageEntity roomMessage);

	Optional<RoomMessageReactionEntity> findByIdAndRoomParticipant(long id, RoomParticipantEntity roomParticipant);
}
