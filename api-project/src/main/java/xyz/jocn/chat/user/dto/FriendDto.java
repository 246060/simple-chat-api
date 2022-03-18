package xyz.jocn.chat.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class FriendDto {

	@NotNull
	private Long id;

	@Size(min = 1, max = 20)
	private String name;

	private Boolean favorite;

	private Boolean hidden;
}
