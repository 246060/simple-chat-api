package xyz.jocn.chat.message.repo.message;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.message.entity.MessageEntity;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long>, MessageRepositoryExt {

	Page<MessageEntity> findAllByChannelId(long channelId, Pageable pageable);

	Optional<MessageEntity> findByIdAndSenderId(long id, long participantId);
}
