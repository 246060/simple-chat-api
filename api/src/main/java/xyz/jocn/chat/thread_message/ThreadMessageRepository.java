package xyz.jocn.chat.thread_message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadMessageRepository extends JpaRepository<ThreadMessageEntity, Long> {
}
