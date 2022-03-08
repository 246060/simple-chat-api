package xyz.jocn.chat.room.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.room.entity.ThreadMessageEntity;
import xyz.jocn.chat.room.entity.ThreadMessageMarkEntity;

@Repository
public interface ThreadMessageMarkRepository extends JpaRepository<ThreadMessageMarkEntity, Long> {

	List<ThreadMessageMarkEntity> findAllByThreadMessage(ThreadMessageEntity threadMessage);
}
