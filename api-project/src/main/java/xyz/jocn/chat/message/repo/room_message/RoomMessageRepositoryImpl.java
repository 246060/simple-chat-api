package xyz.jocn.chat.message.repo.room_message;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.message.entity.QRoomMessageEntity;

@Repository
public class RoomMessageRepositoryImpl implements RoomMessageRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QRoomMessageEntity roomMessage;

	public RoomMessageRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.roomMessage = QRoomMessageEntity.roomMessageEntity;
	}
}
