package xyz.jocn.chat.room.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.room.entity.RoomEntity;
import xyz.jocn.chat.room.entity.RoomParticipantEntity;
import xyz.jocn.chat.user.entity.UserEntity;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipantEntity, Long> {

	List<RoomParticipantEntity> findAllByUser(UserEntity user);
	List<RoomParticipantEntity> findAllByRoom(RoomEntity room);

	Optional<RoomParticipantEntity> findByRoomAndUser(RoomEntity room, UserEntity userEntity);
}
