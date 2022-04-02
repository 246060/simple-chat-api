package xyz.jocn.chat.file.repo.storage;

import java.io.InputStream;

import org.springframework.core.io.Resource;

import xyz.jocn.chat.file.FileMetaEntity;

public interface StorageRepository {

	void save(InputStream in, FileMetaEntity fileMetaEntity);

	Resource loadAsResource(FileMetaEntity fileMetaEntity);
}
