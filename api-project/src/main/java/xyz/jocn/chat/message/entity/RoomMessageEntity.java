package xyz.jocn.chat.message.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.chat.chat_space.entity.RoomEntity;
import xyz.jocn.chat.message.enums.ChatMessageType;
import xyz.jocn.chat.message.enums.RoomMessageState;
import xyz.jocn.chat.participant.entity.RoomParticipantEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "room_message")
@Entity
public class RoomMessageEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@Column(length = 1000)
	private String message;

	@Column(nullable = false, length = 10)
	private ChatMessageType type;

	@Column(nullable = false, length = 10)
	private RoomMessageState state = RoomMessageState.ACTIVE;

	private boolean hasThread = false;
	private boolean hasMark = false;

	@CreatedBy
	private long createdBy;

	@CreatedDate
	private Instant createdAt;

	@LastModifiedDate
	private Instant updatedAt;

	@LastModifiedBy
	private long updatedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	private RoomParticipantEntity sender;

	@ManyToOne(fetch = FetchType.LAZY)
	private RoomEntity room;

	public RoomMessageEntity(long id) {
		this.id = id;
	}

	@Builder
	public RoomMessageEntity(long id, String message, ChatMessageType type,
		RoomMessageState state, boolean hasThread, boolean hasMark,
		RoomParticipantEntity sender, RoomEntity room) {
		this.id = id;
		this.message = message;
		this.type = type;
		this.state = state;
		this.hasThread = hasThread;
		this.hasMark = hasMark;
		this.sender = sender;
		this.room = room;
	}

	public boolean hasThread() {
		return hasThread;
	}

	public boolean hasMark() {
		return hasMark;
	}

	public void changeState(RoomMessageState state) {
		this.state = state;
	}

	public void openThread() {
		this.hasThread = true;
	}
}
