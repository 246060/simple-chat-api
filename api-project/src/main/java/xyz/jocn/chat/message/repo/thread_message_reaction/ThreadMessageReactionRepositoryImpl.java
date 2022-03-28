package xyz.jocn.chat.message.repo.thread_message_reaction;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.message.entity.QThreadMessageReactionEntity;

@Repository
public class ThreadMessageReactionRepositoryImpl implements ThreadMessageReactionRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QThreadMessageReactionEntity threadMessageReaction;

	public ThreadMessageReactionRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.threadMessageReaction = QThreadMessageReactionEntity.threadMessageReactionEntity;
	}
}
