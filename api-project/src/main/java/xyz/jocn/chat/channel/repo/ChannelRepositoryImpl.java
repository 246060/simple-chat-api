package xyz.jocn.chat.channel.repo;

import static com.querydsl.core.group.GroupBy.*;
import static xyz.jocn.chat.participant.ParticipantState.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
			.innerJoin(user).on(participant.user().id.eq(user.id), participant.state.eq(JOIN))
			.where(user.id.eq(uid))
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
	public Optional<ChannelDto> findMyChannelById(long id) {
		QParticipantEntity participant = QParticipantEntity.participantEntity;
		QUserEntity user = QUserEntity.userEntity;

		ChannelDto channelDto = queryFactory
			.select(
				Projections.fields(
					ChannelDto.class,
					channel.id))
			.from(channel)
			.where(channel.id.eq(id))
			.fetchOne();

		if (Objects.isNull(channelDto)) {
			return Optional.empty();
		}

		List<ParticipantDto> participantDtos = queryFactory
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
			)
			.from(participant)
			.innerJoin(user).on(participant.user().id.eq(user.id), participant.state.eq(JOIN))
			.where(participant.channel().id.eq(id))
			.fetchJoin()
			.fetch();

		channelDto.setParticipants(participantDtos);
		return Optional.of(channelDto);
	}

}
