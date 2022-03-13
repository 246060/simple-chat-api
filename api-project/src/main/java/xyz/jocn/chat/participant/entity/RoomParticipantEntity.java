package xyz.jocn.chat.participant.entity;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import xyz.jocn.chat.chat_space.entity.RoomEntity;
import xyz.jocn.chat.user.entity.UserEntity;

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
}
