package xyz.jocn.chat.message.repo.thread_message_reaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.message.entity.ThreadMessageEntity;
import xyz.jocn.chat.message.entity.ThreadMessageReactionEntity;
import xyz.jocn.chat.message.repo.thread_message.ThreadMessageRepositoryExt;

@Repository
public interface ThreadMessageReactionRepository
	extends JpaRepository<ThreadMessageReactionEntity, Long>, ThreadMessageRepositoryExt {

	List<ThreadMessageReactionEntity> findAllByThreadMessage(ThreadMessageEntity threadMessage);
}
