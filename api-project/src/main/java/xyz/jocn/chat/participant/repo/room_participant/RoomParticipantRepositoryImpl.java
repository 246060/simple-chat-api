package xyz.jocn.chat.participant.repo.room_participant;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.chat_space.entity.QRoomEntity;
import xyz.jocn.chat.participant.entity.QRoomParticipantEntity;

@Repository
public class RoomParticipantRepositoryImpl implements RoomParticipantRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QRoomParticipantEntity roomParticipant;

	public RoomParticipantRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.roomParticipant = QRoomParticipantEntity.roomParticipantEntity;
	}
}
