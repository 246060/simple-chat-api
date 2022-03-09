package xyz.jocn.chat.participant.repo.thread_participant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.chat_space.entity.ThreadEntity;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;

@Repository
public interface ThreadParticipantRepository
	extends JpaRepository<ThreadParticipantEntity, Long>, ThreadParticipantRepositoryExt {

	Optional<ThreadParticipantEntity> findByThreadIdAAndUserId(Long threadId, Long userId);

	List<ThreadParticipantEntity> findAllByUserId(Long userId);

	int deleteAllByUserIdAndThreadIn(Long userId, List<ThreadEntity> threadEntities);
}
