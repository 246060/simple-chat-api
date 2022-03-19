package xyz.jocn.chat.file.repo;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import xyz.jocn.chat.file.QFileEntity;

@Repository
public class FileMetaRepositoryImpl implements FileMetaRepositoryExt {

	private final JPAQueryFactory queryFactory;
	private final QFileEntity file;

	public FileMetaRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.file = QFileEntity.fileEntity;
	}
}
