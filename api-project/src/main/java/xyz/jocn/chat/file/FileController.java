package xyz.jocn.chat.file;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;
import static xyz.jocn.chat.file.ReqType.*;

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

	@PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity upload(MultipartFile file, @AuthenticationPrincipal(expression = UID) String uid) {
		return ok(success(fileService.save(Long.parseLong(uid), file)));
	}

	@GetMapping("/{fileId}")
	public ResponseEntity fetchFileMeta(@PathVariable Long fileId) {
		return ok(success(fileService.fetchFileMeta(fileId)));
	}

	@GetMapping("/{fileId}/data")
	public ResponseEntity fetchFile(@PathVariable Long fileId, @RequestParam(defaultValue = "data") ReqType type) {

		FileDto dto = fileService.loadAsResource(fileId);
		BodyBuilder ok = ok();

		if (data == type) {
			MediaType mediaType = parseMediaType(dto.getContentType());
			if (mediaType == IMAGE_GIF || mediaType == IMAGE_JPEG || mediaType == IMAGE_PNG) {
				ok.contentType(mediaType);
			} else {
				throw new FileStorageException("only serve image file. try to download type");
			}
		} else {
			String contentDisposition = String.format("attachment; filename=\"%s\"", dto.getOriginName());
			ok.header(CONTENT_DISPOSITION, contentDisposition);
		}

		return ok.body(dto.getResource());
	}
}
