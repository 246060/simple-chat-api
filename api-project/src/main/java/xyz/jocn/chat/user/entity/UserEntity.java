package xyz.jocn.chat.user.entity;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static xyz.jocn.chat.user.enums.UserRole.*;
import static xyz.jocn.chat.user.enums.UserState.*;

import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.chat.user.enums.UserRole;
import xyz.jocn.chat.user.enums.UserState;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(name = "uk_user_email", columnNames = "email")})
@Entity
public class UserEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@Column(length = 100)
	private String email;

	@Column(length = 50)
	private String name;

	@Column(length = 100)
	private String password;

	@Builder.Default
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private UserState state = ACTIVE;

	@Builder.Default
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private UserRole role = USER;

	private String profileImgUrl;
	private String stateMessage;

	private Instant lastLoginTime;

	@CreatedDate
	private Instant createdAt;

	@LastModifiedDate
	private Instant updatedAt;

	@LastModifiedBy
	private Long updatedBy;


	@ToString.Exclude
	@Builder.Default
	@OneToOne(fetch = LAZY, cascade = {PERSIST, REMOVE})
	private UserSettingEntity setting = UserSettingEntity.builder().build();






	public UserEntity(long id) {
		this.id = id;
	}

	public void delete() {
		this.state = DELETED;
	}

	public void changeName(String name) {
		this.name = name;
	}

	public void changeStateMessage(String stateMessage) {
		this.stateMessage = stateMessage;
	}

	public void changeProfileImgUrl(String profileImgUrl) {
		this.profileImgUrl = profileImgUrl;
	}
}
