package xyz.jocn.chat.message.repo.message;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.message.entity.QMessageEntity;

@Repository
public class MessageRepositoryImpl implements MessageRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QMessageEntity message;

	public MessageRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.message = QMessageEntity.messageEntity;
	}
}
