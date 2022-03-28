package xyz.jocn.chat.message.repo.message_file;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.message.entity.QMessageFileEntity;

@Repository
public class MessageFileRepositoryImpl implements MessageFileRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QMessageFileEntity messageFile;

	public MessageFileRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.messageFile = QMessageFileEntity.messageFileEntity;
	}
}
