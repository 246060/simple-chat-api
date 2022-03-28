package xyz.jocn.chat.file.repo;

import java.io.InputStream;

import org.springframework.core.io.Resource;

import xyz.jocn.chat.file.FileEntity;

public interface StorageRepository {

	void save(InputStream in, FileEntity fileEntity);

	Resource loadAsResource(FileEntity fileEntity);
}
