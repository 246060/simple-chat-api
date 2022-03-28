package xyz.jocn.chat.message.repo.message_file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.message.entity.MessageFileEntity;

@Repository
public interface MessageFileRepository extends JpaRepository<MessageFileEntity, Long>, MessageFileRepositoryExt {
}
