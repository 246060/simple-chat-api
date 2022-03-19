package xyz.jocn.chat.file.repo;

import java.io.InputStream;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.file.FileEntity;

@Slf4j
public class StorageAwsS3Repository implements StorageRepository {

	@Override
	public void save(InputStream in, FileEntity fileEntity) {
		log.info("{}", new Object() {
		}.getClass().getEnclosingMethod().getName());
	}

	@Override
	public Resource loadAsResource(FileEntity fileEntity) {
		log.info("{}", new Object() {
		}.getClass().getEnclosingMethod().getName());
		return null;
	}

	@Override
	public void delete() {
		log.info("{}", new Object() {
		}.getClass().getEnclosingMethod().getName());
	}
}
