package xyz.jocn.chat.file;

import static java.nio.charset.StandardCharsets.*;
import static xyz.jocn.chat.common.exception.ResourceType.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.FileStorageException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.common.util.FileUtil;
import xyz.jocn.chat.file.dto.FileDto;
import xyz.jocn.chat.file.repo.meta.FileMetaRepository;
import xyz.jocn.chat.file.repo.storage.StorageRepository;
import xyz.jocn.chat.user.entity.UserEntity;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FileService {

	private final FileMetaRepository fileRepository;
	private final StorageRepository storageRepository;

	private FileConverter fileConverter = FileConverter.INSTANCE;

	/*
	 * Command =============================================================================
	 * */

	@Transactional
	public FileDto save(long uid, MultipartFile multipartFile) {
		FileMetaEntity fileMetaEntity = this.save(multipartFile, uid);
		return fileConverter.toDto(fileMetaEntity);
	}

	@Transactional
	public FileMetaEntity save(MultipartFile multipartFile, long uid) {

		FileMetaEntity fileMetaEntity = FileMetaEntity.builder()
			.originName(multipartFile.getOriginalFilename())
			.savedName(FileUtil.generateSavedFileName(multipartFile.getOriginalFilename()))
			.savedDir(FileUtil.getSavedDir().toString())
			.byteSize(multipartFile.getSize())
			.contentType(multipartFile.getContentType())
			.user(UserEntity.builder().id(uid).build())
			.build();

		try {
			storageRepository.save(multipartFile.getInputStream(), fileMetaEntity);
			fileRepository.save(fileMetaEntity);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new FileStorageException("Could not store the file. Error: " + e.getMessage());
		}

		return fileMetaEntity;
	}

	@Transactional
	public FileMetaEntity save(long uid, String text) {

		try {
			Path tempFilePath = FileUtil.generateTempFile("txt");
			Files.writeString(tempFilePath, text, UTF_8);

			FileMetaEntity fileMetaEntity = FileMetaEntity.builder()
				.originName(tempFilePath.getFileName().toString())
				.savedName(tempFilePath.getFileName().toString())
				.savedDir(FileUtil.getSavedDir().toString())
				.byteSize(Files.size(tempFilePath))
				.contentType(Files.probeContentType(tempFilePath))
				.user(UserEntity.builder().id(uid).build())
				.build();

			storageRepository.save(Files.newInputStream(tempFilePath), fileMetaEntity);
			fileRepository.save(fileMetaEntity);

			Files.deleteIfExists(tempFilePath);

			return fileMetaEntity;

		} catch (IOException e) {
			log.error(e.getMessage());
			throw new FileStorageException(
				"Could not store the file for channel long message. Error: " + e.getMessage()
			);
		}
	}

	/*
	 * Query ===============================================================================
	 * */

	public FileDto loadAsResource(Long fileId) {
		FileMetaEntity fileMetaEntity = fileRepository
			.findById(fileId)
			.orElseThrow(() -> new ResourceNotFoundException(FILE));

		FileDto fileDto = fileConverter.toDto(fileMetaEntity);
		fileDto.setResource(storageRepository.loadAsResource(fileMetaEntity));
		return fileDto;
	}

	public FileDto fetchFileMeta(Long fileId) {
		return fileConverter.toDto(
			fileRepository.findById(fileId).orElseThrow(() -> new ResourceNotFoundException(FILE))
		);
	}
}
