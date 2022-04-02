package xyz.jocn.chat.file.repo.meta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.jocn.chat.file.FileMetaEntity;

@Repository
public interface FileMetaRepository extends JpaRepository<FileMetaEntity, Long>, FileMetaRepositoryExt {
}
