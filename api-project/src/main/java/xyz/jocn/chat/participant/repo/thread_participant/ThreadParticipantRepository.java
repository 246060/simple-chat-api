package xyz.jocn.chat.participant.repo.thread_participant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.participant.entity.RoomParticipantEntity;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;
import xyz.jocn.chat.thread.ThreadEntity;
import xyz.jocn.chat.user.UserEntity;
import xyz.jocn.chat.user.enums.UserState;

@Repository
public interface ThreadParticipantRepository
	extends JpaRepository<ThreadParticipantEntity, Long>, ThreadParticipantRepositoryExt {

	Optional<ThreadParticipantEntity> findByThreadAndUser(ThreadEntity thread, UserEntity userEntity);

	List<ThreadParticipantEntity> findAllByUserId(Long userId);

	List<ThreadParticipantEntity> findAllByRoomParticipantIn(List<RoomParticipantEntity> entities);

	List<ThreadParticipantEntity> findAllByRoomParticipant(RoomParticipantEntity roomParticipantEntity);

	List<ThreadParticipantEntity> findAllByThreadId(Long threadId);

	int deleteAllByUserIdAndThreadIn(Long userId, List<ThreadEntity> threadEntities);

}
