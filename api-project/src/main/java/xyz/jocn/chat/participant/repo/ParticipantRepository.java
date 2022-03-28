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

	Optional<ParticipantEntity> findByChannelIdAndUserId(long channelId, long userId);

	Optional<ParticipantEntity> findByIdAndUserId(long id, long userId);
}
