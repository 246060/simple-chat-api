package xyz.jocn.chat.message.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.message.MessageEntity;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long>, MessageRepositoryExt {

	Optional<MessageEntity> findByIdAndSenderId(long id, long participantId);
}
