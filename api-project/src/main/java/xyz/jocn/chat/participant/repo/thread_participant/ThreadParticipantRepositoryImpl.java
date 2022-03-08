package xyz.jocn.chat.participant.repo.thread_participant;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.participant.entity.QThreadParticipantEntity;

@Repository
public class ThreadParticipantRepositoryImpl implements ThreadParticipantRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QThreadParticipantEntity threadParticipant;

	public ThreadParticipantRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.threadParticipant = QThreadParticipantEntity.threadParticipantEntity;
	}
}
