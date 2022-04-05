package xyz.jocn.chat.message.repo.unread_message;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.message.UnReadMessageEntity;

@Repository
public interface UnReadMessageRepository extends JpaRepository<UnReadMessageEntity, Long> {

	void deleteByMessageIdAndParticipantId(long MessageId, long participantId);

	void deleteByMessageId(long MessageId);

	Optional<Long> countByMessageId(long MessageId);
}
