package xyz.jocn.chat.room.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.room.entity.RoomParticipantEntity;
import xyz.jocn.chat.room.entity.ThreadEntity;
import xyz.jocn.chat.room.entity.ThreadParticipantEntity;

@Repository
public interface ThreadParticipantRepository extends JpaRepository<ThreadParticipantEntity, Long> {

	List<ThreadParticipantEntity> findAllByRoomParticipant(RoomParticipantEntity participant);

	Optional<ThreadParticipantEntity> findByThreadAndRoomParticipant(
		ThreadEntity thread, RoomParticipantEntity roomParticipant
	);
}
