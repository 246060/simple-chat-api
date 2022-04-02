package xyz.jocn.chat.file.repo.storage;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.file.FileMetaEntity;
import xyz.jocn.chat.file.properties.StorageLocalProperties;

@Slf4j
@Component
public class StorageAwsS3Repository implements StorageRepository {

	public StorageAwsS3Repository(StorageLocalProperties properties) {
		log.info("Created Bean : StorageAwsS3Repository");
		log.info("Injected Bean : " + properties.getClass().getSimpleName());
	}

	@Override
	public void save(InputStream in, FileMetaEntity fileMetaEntity) {
		log.info("{}", new Object() {
		}.getClass().getEnclosingMethod().getName());
	}

	@Override
	public Resource loadAsResource(FileMetaEntity fileMetaEntity) {
		log.info("{}", new Object() {
		}.getClass().getEnclosingMethod().getName());
		return null;
	}

}
