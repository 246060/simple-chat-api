package xyz.jocn.chat.friend.entity;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.chat.user.UserEntity;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "firend")
@Entity
public class FriendEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	private String name;
	private boolean favorite = false;
	private boolean hidden = false;

	@CreatedDate
	private Instant createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity source;

	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity target;

	public void changeHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public void cancelHidden() {
		this.hidden = false;
	}

	public void changeFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public void cancelFavorite() {
		this.favorite = false;
	}

	public void changeName(String newName) {
		this.name = newName;
	}
}
