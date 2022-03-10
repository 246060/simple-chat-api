package xyz.jocn.chat.file.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import xyz.jocn.chat.file.repo.FileRepository;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

	@Mock
	FileRepository fileRepository;
	@InjectMocks
	FileService service;
}