package xyz.jocn.chat.friend.repo;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.friend.FriendEntity;
import xyz.jocn.chat.friend.QFriendEntity;
import xyz.jocn.chat.friend.dto.FriendSearchDto;

@Repository
public class FriendRepositoryImpl implements FriendRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QFriendEntity friend;

	public FriendRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.friend = QFriendEntity.friendEntity;
	}

	@Override
	public List<FriendEntity> findAllBySourceIdAndCondition(long sourceId, FriendSearchDto condition) {
		return queryFactory.selectFrom(friend)
			.where(friend.source().id.eq(sourceId))
			.where(eqFavorite(condition.getFavorite()))
			.where(eqHidden(condition.getHidden()))
			.fetch();
	}

	private BooleanExpression eqFavorite(Boolean favorite) {
		if (Objects.isNull(favorite)) {
			return null;
		}
		return friend.favorite.eq(favorite);
	}

	private BooleanExpression eqHidden(Boolean hidden) {
		if (Objects.isNull(hidden)) {
			return null;
		}
		return friend.hidden.eq(hidden);
	}

}
