package xyz.jocn.chat.user.repo.friend_block;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.chat_space.entity.QRoomEntity;
import xyz.jocn.chat.user.entity.QFriendBlockEntity;

@Repository
public class FriendBlockRepositoryImpl implements FriendBlockRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QFriendBlockEntity friendBlock;

	public FriendBlockRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.friendBlock = QFriendBlockEntity.friendBlockEntity;
	}
}
