package xyz.jocn.chat.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.room.entity.ThreadMessageFileEntity;

@Repository
public interface ThreadMessageFileRepository extends JpaRepository<ThreadMessageFileEntity, Long> {
}
