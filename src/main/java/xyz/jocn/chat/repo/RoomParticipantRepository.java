package xyz.jocn.chat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.entity.RoomParticipantEntity;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipantEntity, Long> {
}
