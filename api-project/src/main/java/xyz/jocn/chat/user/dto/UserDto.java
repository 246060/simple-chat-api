package xyz.jocn.chat.user.dto;

import java.time.Instant;

import lombok.Data;
import xyz.jocn.chat.user.enums.UserRole;
import xyz.jocn.chat.user.enums.UserState;

@Data
public class UserDto {
	private Long id;
	private String email;
	private String name;
	private UserState state;
	private UserRole role;
	private String profileImageUrl;
	private String stateMessage;
	private Instant lastLoginTime;
	private Instant createdAt;
	private Instant updatedAt;
	private Long updatedBy;
}
