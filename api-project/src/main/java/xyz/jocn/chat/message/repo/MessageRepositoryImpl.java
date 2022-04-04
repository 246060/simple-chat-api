package xyz.jocn.chat.message.repo;

import static xyz.jocn.chat.common.dto.SliceDirection.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.common.dto.SliceCriteria;
import xyz.jocn.chat.common.dto.SliceDirection;
import xyz.jocn.chat.message.MessageEntity;
import xyz.jocn.chat.message.QMessageEntity;
import xyz.jocn.chat.message.dto.MessageDto;
import xyz.jocn.chat.participant.QParticipantEntity;
import xyz.jocn.chat.participant.dto.ParticipantDto;
import xyz.jocn.chat.user.QUserEntity;
import xyz.jocn.chat.user.dto.UserDto;

@Repository
public class MessageRepositoryImpl implements MessageRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QMessageEntity message;

	public MessageRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.message = QMessageEntity.messageEntity;
	}

	@Override
	public List<MessageEntity> findMessagesInChannel(long channelId, SliceCriteria<Long> criteria) {
		QParticipantEntity participant = QParticipantEntity.participantEntity;
		QUserEntity user = QUserEntity.userEntity;

		return queryFactory
			.selectFrom(message)

			.innerJoin(participant)
			.on(message.sender().id.eq(participant.id))

			.innerJoin(user)
			.on(participant.user().id.eq(user.id))

			.where(message.channel().id.eq(channelId))
			.where(upOrDown(criteria.getBasePoint(), criteria.getDirection()))

			.orderBy(message.id.desc())
			.limit(criteria.getLimit())

			.fetchJoin()
			.fetch();
	}

	@Override
	public List<MessageDto> findMessageDtosInChannel(long channelId, SliceCriteria<Long> criteria) {
		QParticipantEntity participant = QParticipantEntity.participantEntity;
		QUserEntity user = QUserEntity.userEntity;

		return queryFactory
			.select(
				Projections.fields(
					MessageDto.class,
					message.id,
					message.text,
					message.type,
					message.state,
					message.hasReaction,
					message.parentId,
					message.createdAt,

					Projections.fields(
						ParticipantDto.class,
						participant.id,

						Projections.fields(
							UserDto.class,
							user.id,
							user.name,
							user.profileImgUrl
						).as("user")
					).as("sender")
				))
			.from(message)

			.innerJoin(participant)
			.on(message.sender().id.eq(participant.id))

			.innerJoin(user)
			.on(participant.user().id.eq(user.id))

			.where(message.channel().id.eq(channelId))
			.where(upOrDown(criteria.getBasePoint(), criteria.getDirection()))

			.orderBy(message.id.desc())
			.limit(criteria.getLimit())

			.fetchJoin()
			.fetch();
	}

	@Override
	public Long findLastMessageIdInChannel(long channelId) {
		return queryFactory
			.select(message.id)
			.from(message)
			.where(message.channel().id.eq(channelId))
			.orderBy(message.id.desc())
			.fetchFirst();
	}

	private BooleanExpression upOrDown(Long pivotMessageId, SliceDirection direction) {
		if (pivotMessageId == null || pivotMessageId == 0) {
			return null;
		}

		if (direction == null) {
			return null;
		}

		if (direction == up) {
			return message.id.gt(pivotMessageId);
		} else {
			return message.id.lt(pivotMessageId);
		}
	}
}
