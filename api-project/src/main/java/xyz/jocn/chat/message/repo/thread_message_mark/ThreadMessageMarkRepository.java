package xyz.jocn.chat.message.repo.thread_message_mark;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.message.entity.ThreadMessageEntity;
import xyz.jocn.chat.message.entity.ThreadMessageMarkEntity;

@Repository
public interface ThreadMessageMarkRepository
	extends JpaRepository<ThreadMessageMarkEntity, Long>, ThreadMessageMarkRepositoryExt {

	List<ThreadMessageMarkEntity> findAllByThreadMessage(ThreadMessageEntity threadMessage);
}
