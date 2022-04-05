package xyz.jocn.chat.message;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "channel_unread_message")
@Entity
public class UnReadMessageEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long id;
	private Long messageId;
	private Long participantId;
}
