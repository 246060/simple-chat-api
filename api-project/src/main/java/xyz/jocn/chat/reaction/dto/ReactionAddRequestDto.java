package xyz.jocn.chat.reaction.dto;

import lombok.Data;
import xyz.jocn.chat.reaction.ReactionType;

@Data
public class ReactionAddRequestDto {
	private ReactionType type;
}
