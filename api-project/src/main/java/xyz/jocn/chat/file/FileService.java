package xyz.jocn.chat.file;

import static xyz.jocn.chat.common.exception.ResourceType.*;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.FileStorageException;
import xyz.jocn.chat.common.exception.ResourceNotFoundException;
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

		LocalDate now = LocalDate.now(ZoneOffset.UTC);

		Path savedDir = Path
			.of(Integer.toString(now.getYear()))
			.resolve(Integer.toString(now.getMonth().getValue()))
			.resolve(Integer.toString(now.getDayOfMonth()))
			.normalize();

		String savedName = new StringBuilder()
			.append(UUID.randomUUID().toString().replaceAll("-", ""))
			.append(".")
			.append(StringUtils.getFilenameExtension(multipartFile.getOriginalFilename()))
			.toString();

		FileEntity fileEntity = FileEntity.builder()
			.originName(multipartFile.getOriginalFilename())
			.savedName(savedName)
			.savedDir(savedDir.toString())
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

		return fileConverter.toDto(fileEntity);
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
