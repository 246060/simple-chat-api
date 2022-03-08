package xyz.jocn.chat.chat_space.repo.thread;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.chat_space.entity.QThreadEntity;

@Repository
public class ThreadRepositoryImpl implements ThreadRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QThreadEntity thread;

	public ThreadRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.thread = QThreadEntity.threadEntity;
	}
}
