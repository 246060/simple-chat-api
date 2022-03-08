package xyz.jocn.chat.room.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.room.entity.ThreadEntity;
import xyz.jocn.chat.room.entity.ThreadMessageEntity;

@Repository
public interface ThreadMessageRepository extends JpaRepository<ThreadMessageEntity, Long> {
	List<ThreadMessageEntity> findByThread(ThreadEntity thread);
}
