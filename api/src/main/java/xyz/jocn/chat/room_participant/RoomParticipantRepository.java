package xyz.jocn.chat.room_participant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipantEntity, Long> {
}
