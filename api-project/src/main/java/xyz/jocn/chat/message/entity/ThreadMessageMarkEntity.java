package xyz.jocn.chat.message.entity;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.chat.message.enums.MessageMarkFlag;
import xyz.jocn.chat.participant.entity.ThreadParticipantEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "thread_message_mark")
@Entity
public class ThreadMessageMarkEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@Enumerated(EnumType.STRING)
	private MessageMarkFlag flag;

	@CreatedDate
	private Instant createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	private ThreadMessageEntity threadMessage;

	@ManyToOne(fetch = FetchType.LAZY)
	private ThreadParticipantEntity threadParticipant;

	@Builder
	public ThreadMessageMarkEntity(long id, MessageMarkFlag flag,
		ThreadMessageEntity threadMessage, ThreadParticipantEntity threadParticipant) {
		this.id = id;
		this.flag = flag;
		this.threadMessage = threadMessage;
		this.threadParticipant = threadParticipant;
	}
}
