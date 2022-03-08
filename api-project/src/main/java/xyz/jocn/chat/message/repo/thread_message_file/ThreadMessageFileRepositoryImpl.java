package xyz.jocn.chat.message.repo.thread_message_file;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.message.entity.QThreadMessageFileEntity;

@Repository
public class ThreadMessageFileRepositoryImpl implements ThreadMessageFileRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QThreadMessageFileEntity threadMessageFile;

	public ThreadMessageFileRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.threadMessageFile = QThreadMessageFileEntity.threadMessageFileEntity;
	}
}
