package xyz.jocn.chat.reaction.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.message.MessageEntity;
import xyz.jocn.chat.reaction.ReactionEntity;
import xyz.jocn.chat.participant.ParticipantEntity;

@Repository
public interface ReactionRepository
	extends JpaRepository<ReactionEntity, Long>, ReactionRepositoryExt {

	List<ReactionEntity> findAllByMessage(MessageEntity message);

	Optional<ReactionEntity> findByIdAndParticipant(long id, ParticipantEntity participant);
}
