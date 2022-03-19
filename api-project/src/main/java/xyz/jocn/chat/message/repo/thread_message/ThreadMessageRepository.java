package xyz.jocn.chat.message.repo.thread_message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.thread.ThreadEntity;
import xyz.jocn.chat.message.entity.ThreadMessageEntity;

@Repository
public interface ThreadMessageRepository extends JpaRepository<ThreadMessageEntity, Long>, ThreadMessageRepositoryExt {
	List<ThreadMessageEntity> findByThread(ThreadEntity thread);
}
