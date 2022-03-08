package xyz.jocn.chat.message.repo.thread_message_file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.message.entity.ThreadMessageFileEntity;

@Repository
public interface ThreadMessageFileRepository
	extends JpaRepository<ThreadMessageFileEntity, Long>, ThreadMessageFileRepositoryExt {
}
