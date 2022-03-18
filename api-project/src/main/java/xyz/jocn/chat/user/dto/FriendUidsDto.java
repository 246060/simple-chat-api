package xyz.jocn.chat.user.dto;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class FriendUidsDto {
	@NotEmpty
	private List<@Min(1) Long> targets;
}
