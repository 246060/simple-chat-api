package xyz.jocn.chat.thread.repo;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.thread.QThreadEntity;

@Repository
public class ThreadRepositoryImpl implements ThreadRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QThreadEntity thread;

	public ThreadRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.thread = QThreadEntity.threadEntity;
	}
}
