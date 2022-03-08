package xyz.jocn.chat.auth;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.chat.user.entity.UserEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "token")
@Entity
public class TokenEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@ManyToOne
	private UserEntity user;

	private String refreshToken;
	private Instant refreshExpireTime;

	@CreatedDate
	private Instant createdAt;

	@LastModifiedDate
	private Instant updatedAt;

	@Builder
	public TokenEntity(long id, UserEntity user, String refreshToken, Instant refreshExpireTime) {
		this.id = id;
		this.user = user;
		this.refreshToken = refreshToken;
		this.refreshExpireTime = refreshExpireTime;
	}
}
