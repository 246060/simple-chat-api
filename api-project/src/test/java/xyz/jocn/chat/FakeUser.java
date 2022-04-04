package xyz.jocn.chat;

import lombok.Data;
import xyz.jocn.chat.user.entity.UserEntity;

@Data
public class FakeUser {

	public static UserEntity generateUser(String username) {
		UserEntity user = UserEntity.builder()
			.name(username)
			.email(String.format("%s@test.org", username))
			.build();
		return user;
	}
}
