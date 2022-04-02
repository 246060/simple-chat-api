package xyz.jocn.chat.channel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.chat.participant.ParticipantEntity;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "channel")
@Entity
public class ChannelEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@OneToMany(mappedBy = "channel")
	private List<ParticipantEntity> participants;

	@CreatedDate
	private Instant createdAt;

	private Long firstMessageId;

	public ChannelEntity(long id) {
		this.id = id;
	}

	public void join(ParticipantEntity participant) {
		if (Objects.isNull(participants)) {
			participants = new ArrayList<>();
		}
		participants.add(participant);
	}

	public void saveFirstMessageId(Long firstMessageId) {
		if (this.firstMessageId == null || this.firstMessageId == 0L) {
			this.firstMessageId = firstMessageId;
		}
	}
}
