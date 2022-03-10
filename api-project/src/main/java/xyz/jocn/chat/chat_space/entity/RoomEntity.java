package xyz.jocn.chat.chat_space.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
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
@Table(name = "room")
@Entity
public class RoomEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@ManyToOne
	private UserEntity user;

	@CreatedDate
	private Instant createdAt;

	public RoomEntity(long id) {
		this.id = id;
	}

	@Builder
	public RoomEntity(long id, UserEntity user) {
		this.id = id;
		this.user = user;
	}
}
