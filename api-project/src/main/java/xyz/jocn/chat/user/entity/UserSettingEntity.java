package xyz.jocn.chat.user.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_setting")
@Entity
public class UserSettingEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long id;

	@Builder.Default
	private Boolean defaultNotificationActive = true;

	public void enableNotification() {
		this.defaultNotificationActive = true;
	}

	public void disableNotification() {
		this.defaultNotificationActive = false;
	}

}
