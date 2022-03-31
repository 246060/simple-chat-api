package xyz.jocn.chat.message.entity;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static xyz.jocn.chat.message.enums.MessageState.*;

import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.chat.channel.ChannelEntity;
import xyz.jocn.chat.file.FileEntity;
import xyz.jocn.chat.message.enums.ChatMessageType;
import xyz.jocn.chat.message.enums.MessageState;
import xyz.jocn.chat.participant.ParticipantEntity;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "message")
@Entity
public class MessageEntity {

	@GeneratedValue(strategy = AUTO)
	@Id
	private long id;

	@Column(length = 1000)
	private String message;

	@Column(nullable = false, length = 10)
	private ChatMessageType type;

	@Builder.Default
	@Column(nullable = false, length = 10)
	private MessageState state = ACTIVE;

	private boolean hasReaction = false;

	@CreatedDate
	private Instant createdAt;

	@LastModifiedDate
	private Instant updatedAt;

	@LastModifiedBy
	private long updatedBy;

	@ManyToOne(fetch = LAZY)
	private ParticipantEntity sender;

	@ManyToOne(fetch = LAZY)
	private ChannelEntity channel;

	public MessageEntity(long id) {
		this.id = id;
	}

	public boolean hasReaction() {
		return hasReaction;
	}

	public void changeState(MessageState state) {
		this.state = state;
	}

}
