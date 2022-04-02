package xyz.jocn.chat.file.repo.storage;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.file.FileMetaEntity;
import xyz.jocn.chat.file.properties.StorageLocalProperties;

@Slf4j
@Component
public class StorageFtpRepository implements StorageRepository{

	public StorageFtpRepository(StorageLocalProperties properties) {
		log.info("Created Bean : StorageFtpRepository");
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
