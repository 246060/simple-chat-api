package xyz.jocn.chat.chat_space.repo.thread;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.chat_space.entity.ThreadEntity;

@Repository
public interface ThreadRepository extends JpaRepository<ThreadEntity, Long>, ThreadRepositoryExt {

	List<ThreadEntity> findAllByRoomId(Long roomId);
}
