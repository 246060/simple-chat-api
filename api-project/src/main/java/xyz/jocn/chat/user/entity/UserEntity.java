package xyz.jocn.chat.user.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.chat.user.enums.UserState;

@ToString
@Getter
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
	private String name;
	private String password;

	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private UserState state = UserState.ACTIVE;

	@CreatedDate
	private Instant createdAt;

	@LastModifiedDate
	private Instant updatedAt;

	@LastModifiedBy
	private long updatedBy;

	public UserEntity(long id) {
		this.id = id;
	}

	@Builder
	public UserEntity(long id, String email, String name, String password, UserState state) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.state = state;
	}
}
