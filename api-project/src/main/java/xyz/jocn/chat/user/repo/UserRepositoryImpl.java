package xyz.jocn.chat.user.repo;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.user.entity.QUserEntity;

@Repository
public class UserRepositoryImpl implements UserRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QUserEntity user;

	public UserRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.user = QUserEntity.userEntity;
	}
}
