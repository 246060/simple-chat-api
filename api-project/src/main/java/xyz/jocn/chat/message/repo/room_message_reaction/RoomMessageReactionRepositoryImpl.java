package xyz.jocn.chat.message.repo.room_message_reaction;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.message.entity.QRoomMessageReactionEntity;

@Repository
public class RoomMessageReactionRepositoryImpl implements RoomMessageReactionRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QRoomMessageReactionEntity qRoomMessageReactionEntity;

	public RoomMessageReactionRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.qRoomMessageReactionEntity = QRoomMessageReactionEntity.roomMessageReactionEntity;
	}
}
