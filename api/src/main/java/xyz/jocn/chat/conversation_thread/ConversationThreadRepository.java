package xyz.jocn.chat.conversation_thread;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationThreadRepository extends JpaRepository<ConversationThreadEntity, Long> {
}
