package xyz.jocn.chat.participant.repo.room_participant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.chat_space.entity.RoomEntity;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.user.entity.UserEntity;

@Repository
public interface RoomParticipantRepository
	extends JpaRepository<RoomParticipantEntity, Long>, RoomParticipantRepositoryExt {

	List<RoomParticipantEntity> findAllByUser(UserEntity user);

	List<RoomParticipantEntity> findAllByRoom(RoomEntity room);

	Optional<RoomParticipantEntity> findByRoomAndUser(RoomEntity room, UserEntity userEntity);
}
