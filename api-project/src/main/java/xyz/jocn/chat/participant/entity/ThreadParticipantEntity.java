package xyz.jocn.chat.participant.entity;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.chat.chat_space.entity.ThreadEntity;
import xyz.jocn.chat.user.entity.UserEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "thread_participant")
@Entity
public class ThreadParticipantEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@CreatedDate
	private Instant createdAt;

	@ManyToOne
	private ThreadEntity thread;

	@ManyToOne
	private UserEntity user;

	@Builder
	public ThreadParticipantEntity(long id, ThreadEntity thread, UserEntity user) {
		this.id = id;
		this.thread = thread;
		this.user = user;
	}
}
