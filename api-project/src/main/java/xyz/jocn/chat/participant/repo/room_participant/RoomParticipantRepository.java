package xyz.jocn.chat.participant.repo.room_participant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.participant.entity.RoomParticipantEntity;

@Repository
public interface RoomParticipantRepository
	extends JpaRepository<RoomParticipantEntity, Long>, RoomParticipantRepositoryExt {

	List<RoomParticipantEntity> findAllByUserId(Long userId);

	List<RoomParticipantEntity> findAllByRoomId(Long roomId);

	Optional<RoomParticipantEntity> findByRoomIdAndUserId(long roomId, long userId);
}
