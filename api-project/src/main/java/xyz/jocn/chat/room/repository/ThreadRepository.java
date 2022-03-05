package xyz.jocn.chat.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.room.entity.ThreadEntity;

@Repository
public interface ThreadRepository extends JpaRepository<ThreadEntity, Long> {
}
