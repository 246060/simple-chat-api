package xyz.jocn.chat.file.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.file.entity.FileEntity;

@Repository
public interface FileMetaRepository extends JpaRepository<FileEntity, Long>, FileMetaRepositoryExt {
}
