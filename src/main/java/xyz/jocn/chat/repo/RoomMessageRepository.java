package xyz.jocn.chat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.entity.RoomMessageEntity;

@Repository
public interface RoomMessageRepository extends JpaRepository<RoomMessageEntity,Long> {
}
