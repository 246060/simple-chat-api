package xyz.jocn.chat.room.entity;

import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.chat.room.enums.RoomMessageCategory;
import xyz.jocn.chat.room.enums.RoomMessageState;

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
	private RoomMessageCategory category;

	@Column(nullable = false, length = 10)
	private RoomMessageState state;

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

	@ManyToOne
	private RoomParticipantEntity roomParticipant;

	@ManyToOne
	private RoomEntity room;

}
