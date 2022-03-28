package xyz.jocn.chat.channel.repo;

import static com.querydsl.core.group.GroupBy.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.channel.QChannelEntity;
import xyz.jocn.chat.channel.dto.ChannelDto;
import xyz.jocn.chat.participant.QParticipantEntity;
import xyz.jocn.chat.participant.dto.ParticipantDto;
import xyz.jocn.chat.user.QUserEntity;
import xyz.jocn.chat.user.dto.UserDto;

@Repository
public class ChannelRepositoryImpl implements ChannelRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QChannelEntity channel;

	public ChannelRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.channel = QChannelEntity.channelEntity;
	}

	@Override
	public List<ChannelDto> findAllMyChannels(long uid) {
		QParticipantEntity participant = QParticipantEntity.participantEntity;
		QUserEntity user = QUserEntity.userEntity;

		return queryFactory.from(channel)
			.innerJoin(participant).on(channel.id.eq(participant.channel().id))
			.innerJoin(user).on(participant.user().id.eq(user.id))
			.transform(
				groupBy(channel.id).list(
					Projections.fields(
						ChannelDto.class,
						channel.id,
						list(
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
						).as("participants")
					)
				)
			);
	}

	@Override
	public List<ChannelDto> findMyChannelById(long id, long uid) {
		QParticipantEntity participant = QParticipantEntity.participantEntity;
		QUserEntity user = QUserEntity.userEntity;

		return queryFactory.from(channel)
			.innerJoin(participant).on(channel.id.eq(participant.channel().id))
			.innerJoin(user).on(participant.user().id.eq(user.id))
			.where(channel.id.eq(id))
			.transform(
				groupBy(channel.id).list(
					Projections.fields(
						ChannelDto.class,
						channel.id,
						list(
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
						).as("participants")
					)
				)
			);
	}

}
