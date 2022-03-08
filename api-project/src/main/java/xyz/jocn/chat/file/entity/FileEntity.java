package xyz.jocn.chat.file.entity;

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

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.jocn.chat.file.enums.FileState;
import xyz.jocn.chat.file.enums.FileVisibility;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "file")
@Entity
public class FileEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@Column(length = 1000)
	private String originName;

	@Column(length = 1000)
	private String savedName;

	@Column(length = 1000)
	private String savedDir;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private FileVisibility Visibility;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private FileState state;

	private long byteSize;

	@CreatedDate
	private Instant createdAt;

	@CreatedBy
	private long createdBy;

	@LastModifiedDate
	private Instant updatedAt;

	@LastModifiedBy
	private long updatedBy;
}
