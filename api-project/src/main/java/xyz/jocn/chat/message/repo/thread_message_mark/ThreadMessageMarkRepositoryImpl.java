package xyz.jocn.chat.message.repo.thread_message_mark;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.message.entity.QThreadMessageMarkEntity;

@Repository
public class ThreadMessageMarkRepositoryImpl implements ThreadMessageMarkRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QThreadMessageMarkEntity threadMessageMark;

	public ThreadMessageMarkRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.threadMessageMark = QThreadMessageMarkEntity.threadMessageMarkEntity;
	}
}
