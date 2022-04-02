package xyz.jocn.chat.file.repo.storage;

import static java.nio.file.StandardCopyOption.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.FileStorageException;
import xyz.jocn.chat.file.FileMetaEntity;
import xyz.jocn.chat.file.properties.StorageLocalProperties;
import xyz.jocn.chat.file.properties.StorageProperties;

@Slf4j
@Primary
@Component
public class StorageLocalRepository implements StorageRepository {

	private final Path fileRootDir;

	public StorageLocalRepository(StorageProperties properties) {
		log.info("Created Bean : StorageLocalRepository");
		log.info("Injected Bean : " + properties.getClass().getSimpleName());

		StorageLocalProperties localProperties = (StorageLocalProperties)properties;
		fileRootDir = Paths.get(localProperties.getUploadRootPath());

		if (Files.notExists(fileRootDir)) {
			throw new RuntimeException("not exist file root folder for upload!");
		}
	}

	private Path getFilePath(FileMetaEntity fileMetaEntity){
		return fileRootDir
			.resolve(fileMetaEntity.getSavedDir())
			.resolve(fileMetaEntity.getSavedName())
			.normalize();
	}

	@Override
	public void save(InputStream in, FileMetaEntity fileMetaEntity) {

		try {
			Path filePath = getFilePath(fileMetaEntity);
			if (Files.notExists(filePath)) {
				Files.createDirectories(filePath);
			}

			Files.copy(in, filePath, REPLACE_EXISTING);

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new FileStorageException("Could not store the file. Error: " + fileMetaEntity.getOriginName());
		}
	}

	@Override
	public Resource loadAsResource(FileMetaEntity fileMetaEntity) {

		try {
			Path filePath = getFilePath(fileMetaEntity);
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new FileStorageException("Could not read the file!");
			}

		} catch (MalformedURLException e) {
			throw new FileStorageException("Error: " + e.getMessage());
		}
	}

}
