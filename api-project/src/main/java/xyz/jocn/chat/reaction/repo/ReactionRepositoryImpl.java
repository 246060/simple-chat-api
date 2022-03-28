package xyz.jocn.chat.reaction.repo;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.reaction.QReactionEntity;

@Repository
public class ReactionRepositoryImpl implements ReactionRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QReactionEntity reaction;

	public ReactionRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.reaction = QReactionEntity.reactionEntity;
	}
}
