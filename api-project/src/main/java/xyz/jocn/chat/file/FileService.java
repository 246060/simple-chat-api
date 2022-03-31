package xyz.jocn.chat.file;

import static java.nio.charset.StandardCharsets.*;
import static xyz.jocn.chat.common.exception.ResourceType.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.FileStorageException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
import xyz.jocn.chat.common.util.FileUtil;
import xyz.jocn.chat.file.dto.FileDto;
import xyz.jocn.chat.file.repo.FileMetaRepository;
import xyz.jocn.chat.file.repo.StorageRepository;
import xyz.jocn.chat.user.UserEntity;

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
		FileEntity fileEntity = this.save(multipartFile, uid);
		return fileConverter.toDto(fileEntity);
	}

	@Transactional
	public FileEntity save(MultipartFile multipartFile, long uid) {

		FileEntity fileEntity = FileEntity.builder()
			.originName(multipartFile.getOriginalFilename())
			.savedName(FileUtil.generateSavedFileName(multipartFile.getOriginalFilename()))
			.savedDir(FileUtil.getSavedDir().toString())
			.byteSize(multipartFile.getSize())
			.contentType(multipartFile.getContentType())
			.user(UserEntity.builder().id(uid).build())
			.build();

		try {
			storageRepository.save(multipartFile.getInputStream(), fileEntity);
			fileRepository.save(fileEntity);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new FileStorageException("Could not store the file. Error: " + e.getMessage());
		}

		return fileEntity;
	}

	@Transactional
	public FileEntity save(long uid, String text) {

		try {
			Path tempFilePath = FileUtil.generateTempFile("txt");
			Files.writeString(tempFilePath, text, UTF_8);

			FileEntity fileEntity = FileEntity.builder()
				.originName(tempFilePath.getFileName().toString())
				.savedName(tempFilePath.getFileName().toString())
				.savedDir(FileUtil.getSavedDir().toString())
				.byteSize(Files.size(tempFilePath))
				.contentType(Files.probeContentType(tempFilePath))
				.user(UserEntity.builder().id(uid).build())
				.build();

			storageRepository.save(Files.newInputStream(tempFilePath), fileEntity);
			fileRepository.save(fileEntity);

			Files.deleteIfExists(tempFilePath);

			return fileEntity;

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
		FileEntity fileEntity = fileRepository
			.findById(fileId)
			.orElseThrow(() -> new ResourceNotFoundException(FILE));

		FileDto fileDto = fileConverter.toDto(fileEntity);
		fileDto.setResource(storageRepository.loadAsResource(fileEntity));
		return fileDto;
	}

	public FileDto fetchFileMeta(Long fileId) {
		return fileConverter.toDto(
			fileRepository.findById(fileId).orElseThrow(() -> new ResourceNotFoundException(FILE))
		);
	}
}
