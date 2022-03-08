package xyz.jocn.chat.message.repo.room_message_file;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.chat_space.entity.QRoomEntity;
import xyz.jocn.chat.message.entity.QRoomMessageFileEntity;

@Repository
public class RoomMessageFileRepositoryImpl implements RoomMessageFileRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QRoomMessageFileEntity roomMessageFile;

	public RoomMessageFileRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.roomMessageFile = QRoomMessageFileEntity.roomMessageFileEntity;
	}
}
