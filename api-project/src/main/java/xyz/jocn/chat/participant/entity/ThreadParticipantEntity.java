package xyz.jocn.chat.participant.entity;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import xyz.jocn.chat.participant.enums.ParticipantState;
import xyz.jocn.chat.thread.ThreadEntity;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "thread_participant")
@Entity
public class ThreadParticipantEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@Enumerated(EnumType.STRING)
	private ParticipantState state = ParticipantState.JOIN;

	@ManyToOne
	private ThreadEntity thread;

	@ManyToOne
	private RoomParticipantEntity roomParticipant;

	@CreatedDate
	private Instant createdAt;

	public void exit() {
		this.state = ParticipantState.EXIT;
	}
}
