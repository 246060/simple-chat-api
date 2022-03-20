package xyz.jocn.chat.participant.entity;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.chat.room.RoomEntity;
import xyz.jocn.chat.participant.enums.ParticipantState;
import xyz.jocn.chat.user.UserEntity;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "room_participant")
@Entity
public class RoomParticipantEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@CreatedDate
	private Instant createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	private RoomEntity room;

	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity user;

	private String name;

	@Enumerated(EnumType.STRING)
	private ParticipantState state = ParticipantState.JOIN;

	public void exit() {
		this.state = ParticipantState.EXIT;
	}

	public void join(RoomEntity room) {
		this.room = room;
	}
}
