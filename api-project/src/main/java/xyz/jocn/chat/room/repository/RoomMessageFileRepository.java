package xyz.jocn.chat.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.room.entity.RoomMessageFileEntity;

@Repository
public interface RoomMessageFileRepository extends JpaRepository<RoomMessageFileEntity, Long> {
}
