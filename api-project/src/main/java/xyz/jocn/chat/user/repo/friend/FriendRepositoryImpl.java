package xyz.jocn.chat.user.repo.friend;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.user.entity.QFriendEntity;

@Repository
public class FriendRepositoryImpl implements FriendRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QFriendEntity friend;

	public FriendRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.friend = QFriendEntity.friendEntity;
	}
}
