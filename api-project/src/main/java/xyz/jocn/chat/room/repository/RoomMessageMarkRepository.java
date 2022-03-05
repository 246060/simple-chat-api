package xyz.jocn.chat.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.room.entity.RoomMessageMarkEntity;

@Repository
public interface RoomMessageMarkRepository extends JpaRepository<RoomMessageMarkEntity, Long> {
}
