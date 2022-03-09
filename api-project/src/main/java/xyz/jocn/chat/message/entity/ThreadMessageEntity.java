package xyz.jocn.chat.message.entity;

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
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.chat.message.enums.ChatMessageType;
import xyz.jocn.chat.message.enums.ThreadMessageState;
import xyz.jocn.chat.chat_space.entity.ThreadEntity;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "thread_message")
@Entity
public class ThreadMessageEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@Enumerated(EnumType.STRING)
	private ChatMessageType type;

	@Enumerated(EnumType.STRING)
	private ThreadMessageState state = ThreadMessageState.ACTIVE;

	private String message;

	@CreatedDate
	private Instant createdAt;

	@LastModifiedDate
	private Instant updatedAt;

	@LastModifiedBy
	private Instant updatedBy;

	@ManyToOne
	private ThreadParticipantEntity threadParticipant;

	@ManyToOne
	private ThreadEntity thread;

	@Builder
	public ThreadMessageEntity(long id, ChatMessageType type, ThreadMessageState state, String message,
		ThreadParticipantEntity threadParticipant, ThreadEntity thread) {
		this.id = id;
		this.type = type;
		this.state = state;
		this.message = message;
		this.threadParticipant = threadParticipant;
		this.thread = thread;
	}

	public void changeState(ThreadMessageState state) {
		this.state = state;
	}
}
