package xyz.jocn.chat.thread_participant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadParticipantRepository extends JpaRepository<ThreadParticipantEntity, Long> {
}
