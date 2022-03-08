package xyz.jocn.chat.message.repo.thread_message;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.message.entity.QThreadMessageEntity;

@Repository
public class ThreadMessageRepositoryImpl implements ThreadMessageRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QThreadMessageEntity threadMessage;

	public ThreadMessageRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.threadMessage = QThreadMessageEntity.threadMessageEntity;
	}
}
