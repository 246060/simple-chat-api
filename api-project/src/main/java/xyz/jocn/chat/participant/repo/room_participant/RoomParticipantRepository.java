package xyz.jocn.chat.participant.repo.room_participant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;
import xyz.jocn.chat.participant.enums.ParticipantState;
import xyz.jocn.chat.user.enums.UserState;

@Repository
public interface RoomParticipantRepository
	extends JpaRepository<RoomParticipantEntity, Long>, RoomParticipantRepositoryExt {

	List<RoomParticipantEntity> findAllByUserId(Long userId);
	List<RoomParticipantEntity> findAllByRoomId(Long roomId);
	List<RoomParticipantEntity> findAllByUserIdAndState(Long userId, ParticipantState state);
	Optional<RoomParticipantEntity> findByRoomIdAndUserId(long roomId, long userId);

	Optional<RoomParticipantEntity> findByIdAndUserId(long id, long userId);
}
