package xyz.jocn.chat.participant;

import static xyz.jocn.chat.participant.ParticipantState.*;

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
import xyz.jocn.chat.channel.ChannelEntity;
import xyz.jocn.chat.user.entity.UserEntity;

@ToString(exclude = "channel")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "participant")
@Entity
public class ParticipantEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	private ParticipantState state = JOIN;

	@ManyToOne(fetch = FetchType.LAZY)
	private ChannelEntity channel;

	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity user;

	@CreatedDate
	private Instant createdAt;

	private Long lastMessageIdBeforeJoin;

	private Boolean notificationActive;

	public void enableNotification() {
		this.notificationActive = true;
	}

	public void disableNotification() {
		this.notificationActive = false;
	}

	public void exit() {
		this.state = EXIT;
	}

	public void join(ChannelEntity channel) {
		this.channel = channel;
		channel.join(this);
	}

	public void saveLastMessageIdBeforeJoin(Long messageId) {
		this.lastMessageIdBeforeJoin = messageId;
	}
}
