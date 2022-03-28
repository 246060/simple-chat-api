package xyz.jocn.chat.participant.repo.thread_participant;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.participant.entity.QRoomParticipantEntity;
import xyz.jocn.chat.participant.entity.QThreadParticipantEntity;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;
import xyz.jocn.chat.thread.ThreadEntity;
import xyz.jocn.chat.user.QUserEntity;

@Repository
public class ThreadParticipantRepositoryImpl implements ThreadParticipantRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QThreadParticipantEntity threadParticipant;

	public ThreadParticipantRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.threadParticipant = QThreadParticipantEntity.threadParticipantEntity;
	}

	public List<ThreadParticipantEntity> findAllCurrentParticipants(long roomId, long threadId) {
		QRoomParticipantEntity roomParticipant = QRoomParticipantEntity.roomParticipantEntity;
		QUserEntity user = QUserEntity.userEntity;

		return Collections.emptyList();
	}
}
