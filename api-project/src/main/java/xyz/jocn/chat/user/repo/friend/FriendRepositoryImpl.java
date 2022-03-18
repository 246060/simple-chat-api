package xyz.jocn.chat.user.repo.friend;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.user.dto.FriendSearchDto;
import xyz.jocn.chat.user.entity.FriendEntity;
import xyz.jocn.chat.user.entity.QFriendEntity;

@Repository
public class FriendRepositoryImpl implements FriendRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QFriendEntity friend;

	public FriendRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.friend = QFriendEntity.friendEntity;
	}

	@Override
	public List<FriendEntity> findAllBySourceIdAndCondition(Long sourceId, FriendSearchDto condition) {
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
