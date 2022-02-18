package xyz.jocn.chat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.entity.ThreadMessageEntity;

@Repository
public interface ThreadMessageRepository extends JpaRepository<ThreadMessageEntity, Long> {
}
