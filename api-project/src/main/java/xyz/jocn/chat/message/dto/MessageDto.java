package xyz.jocn.chat.message.dto;

import java.time.Instant;

import lombok.Data;
import xyz.jocn.chat.message.enums.MessageState;
import xyz.jocn.chat.message.enums.MessageType;
import xyz.jocn.chat.participant.dto.ParticipantDto;

@Data
public class MessageDto {

	private Long id;
	private String text;
	private MessageType type;
	private MessageState state;
	private Boolean hasReaction;
	private Long parentId;
	private Instant createdAt;
	private ParticipantDto sender;
}
