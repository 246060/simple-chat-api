package xyz.jocn.chat.message.repo.room_message_mark;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.message.entity.QRoomMessageMarkEntity;

@Repository
public class RoomMessageMarkRepositoryImpl implements RoomMessageMarkRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QRoomMessageMarkEntity roomMessageMark;

	public RoomMessageMarkRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.roomMessageMark = QRoomMessageMarkEntity.roomMessageMarkEntity;
	}
}
