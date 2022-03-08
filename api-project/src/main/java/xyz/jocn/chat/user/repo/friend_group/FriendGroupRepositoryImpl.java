package xyz.jocn.chat.user.repo.friend_group;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.user.entity.QFriendGroupEntity;

@Repository
public class FriendGroupRepositoryImpl implements FriendGroupRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QFriendGroupEntity friendGroup;

	public FriendGroupRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.friendGroup = QFriendGroupEntity.friendGroupEntity;
	}
}
