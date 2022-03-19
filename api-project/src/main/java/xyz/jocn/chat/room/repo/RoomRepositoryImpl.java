package xyz.jocn.chat.room.repo;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.room.QRoomEntity;

@Repository
public class RoomRepositoryImpl implements RoomRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QRoomEntity room;

	public RoomRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.room = QRoomEntity.roomEntity;
	}
}
