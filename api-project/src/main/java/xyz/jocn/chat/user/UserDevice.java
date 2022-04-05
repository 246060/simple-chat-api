package xyz.jocn.chat.user;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

import javax.persistence.OneToOne;

import lombok.Builder;
import lombok.ToString;
import xyz.jocn.chat.user.entity.UserSettingEntity;

public class UserDevice {



	@ToString.Exclude
	@Builder.Default
	@OneToOne(fetch = LAZY, cascade = {PERSIST, REMOVE})
	private UserSettingEntity setting = UserSettingEntity.builder().build();
}
