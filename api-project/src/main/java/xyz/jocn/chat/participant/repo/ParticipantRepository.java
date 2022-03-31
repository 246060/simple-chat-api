package xyz.jocn.chat.participant.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.participant.ParticipantEntity;
import xyz.jocn.chat.participant.ParticipantState;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long>, ParticipantRepositoryExt {

	List<ParticipantEntity> findAllByUserId(long userId);

	Optional<ParticipantEntity> findByChannelIdAndUserIdAndState(long channelId, long userId, ParticipantState state);

	Optional<ParticipantEntity> findByIdAndChannelId(long id, long channelId);
}
