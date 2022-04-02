package xyz.jocn.chat.file;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
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
import xyz.jocn.chat.file.enums.FileState;
import xyz.jocn.chat.file.enums.FileVisibility;
import xyz.jocn.chat.user.UserEntity;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "file")
@Entity
public class FileMetaEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@Column(length = 1000)
	private String originName;

	@Column(length = 1000)
	private String savedName;

	@Column(length = 1000)
	private String savedDir;

	private String contentType;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private FileVisibility Visibility;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private FileState state = FileState.Active;

	private long byteSize;

	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity user;

	@CreatedDate
	private Instant createdAt;

	@LastModifiedDate
	private Instant updatedAt;

	@LastModifiedBy
	private long updatedBy;
}
