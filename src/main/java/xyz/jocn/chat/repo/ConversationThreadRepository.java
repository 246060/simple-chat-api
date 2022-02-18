package xyz.jocn.chat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.entity.ConversationThreadEntity;

@Repository
public interface ConversationThreadRepository extends JpaRepository<ConversationThreadEntity, Long> {
}
