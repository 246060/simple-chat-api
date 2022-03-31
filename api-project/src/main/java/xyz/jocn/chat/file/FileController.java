package xyz.jocn.chat.file;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.http.ResponseEntity.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.FileStorageException;
import xyz.jocn.chat.file.dto.FileDto;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/files")
@RestController
public class FileController {

	private final FileService fileService;
	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;

	@PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity upload(MultipartFile file, @AuthenticationPrincipal(expression = UID) String uid) {
		FileDto fileDto = fileService.save(Long.parseLong(uid), file);
		URI uri = fromCurrentRequest().path("/{id}").buildAndExpand(fileDto.getId()).toUri();
		return created(uri).body(success());
	}

	@GetMapping("/{fileId}")
	public ResponseEntity fetchFileMeta(@PathVariable Long fileId) {
		return ok(success(fileService.fetchFileMeta(fileId)));
	}

	@GetMapping("/{fileId}/data")
	public ResponseEntity fetchFile(
		@PathVariable Long fileId,
		@RequestParam(required = false, defaultValue = "false") Boolean download
	) {
		FileDto dto = fileService.loadAsResource(fileId);
		BodyBuilder ok = ok();

		if (download) {
			String contentDisposition = String.format("attachment; filename=\"%s\"", dto.getOriginName());
			ok.header(CONTENT_DISPOSITION, contentDisposition);

		} else {
			if (isImage(parseMediaType(dto.getContentType()))) {
				ok.contentType(parseMediaType(dto.getContentType()));
			} else {
				throw new FileStorageException("only serve image file. try to download type");
			}
		}

		return ok.body(dto.getResource());
	}

	private boolean isImage(MediaType mediaType) {
		return mediaType == IMAGE_GIF || mediaType == IMAGE_JPEG || mediaType == IMAGE_PNG;
	}
}
