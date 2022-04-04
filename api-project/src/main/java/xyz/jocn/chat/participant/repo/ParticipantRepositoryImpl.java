package xyz.jocn.chat.participant.repo;

import static xyz.jocn.chat.participant.ParticipantState.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.participant.QParticipantEntity;
import xyz.jocn.chat.participant.dto.ParticipantDto;
import xyz.jocn.chat.user.dto.UserDto;
import xyz.jocn.chat.user.entity.QUserEntity;

@Repository
public class ParticipantRepositoryImpl implements ParticipantRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QParticipantEntity participant;

	public ParticipantRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.participant = QParticipantEntity.participantEntity;
	}

	@Override
	public List<ParticipantDto> findParticipantsInChannel(long channelId) {
		QUserEntity user = QUserEntity.userEntity;

		return queryFactory
			.select(
				Projections.fields(
					ParticipantDto.class,
					participant.id,
					Projections.fields(
						UserDto.class,
						user.id,
						user.name,
						user.email,
						user.profileImgUrl
					).as("user")
				)
			).from(participant)
			.innerJoin(user).on(participant.user().id.eq(user.id), participant.state.eq(JOIN))
			.where(participant.channel().id.eq(channelId))
			.fetchJoin()
			.fetch();
	}

}
