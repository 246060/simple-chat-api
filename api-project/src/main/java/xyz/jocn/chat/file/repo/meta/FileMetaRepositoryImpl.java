package xyz.jocn.chat.file.repo.meta;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.file.QFileMetaEntity;

@Repository
public class FileMetaRepositoryImpl implements FileMetaRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QFileMetaEntity fileMeta;

	public FileMetaRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.fileMeta = QFileMetaEntity.fileMetaEntity;
	}
}
