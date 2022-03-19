package xyz.jocn.chat.file.repo;

import static java.nio.file.StandardCopyOption.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.FileStorageException;
import xyz.jocn.chat.file.FileEntity;

@Slf4j
public class StorageLocalRepository implements StorageRepository {

	private final Path fileRootDir = Paths.get("file-root-dir");

	public StorageLocalRepository() {
		if (Files.notExists(fileRootDir)) {
			throw new RuntimeException("not exist file root folder for upload!");
		}
	}

	@Override
	public void save(InputStream in, FileEntity fileEntity) {
		log.info("{}", new Object() {
		}.getClass().getEnclosingMethod().getName());

		try {
			Path filePath = fileRootDir
				.resolve(fileEntity.getSavedDir())
				.resolve(fileEntity.getSavedName())
				.normalize();

			if (Files.notExists(filePath)) {
				Files.createDirectories(filePath);
			}

			Files.copy(in, filePath, REPLACE_EXISTING);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new FileStorageException("Could not store the file. Error: " + fileEntity.getOriginName());
		}
	}

	@Override
	public Resource loadAsResource(FileEntity fileEntity) {
		log.info("{}", new Object() {
		}.getClass().getEnclosingMethod().getName());

		try {
			Path file = fileRootDir
				.resolve(fileEntity.getSavedDir())
				.resolve(fileEntity.getSavedName());

			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new FileStorageException("Could not read the file!");
			}

		} catch (MalformedURLException e) {
			throw new FileStorageException("Error: " + e.getMessage());
		}
	}

	@Override
	public void delete() {
		log.info("{}", new Object() {
		}.getClass().getEnclosingMethod().getName());
	}
}
