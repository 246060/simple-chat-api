package xyz.jocn.chat.file.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.file.repo.FileMetaRepository;
import xyz.jocn.chat.file.repo.StorageRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

	private final FileMetaRepository fileRepository;
	private final StorageRepository storageRepository;

}
