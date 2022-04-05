package xyz.jocn.chat.user;

import static org.springframework.http.ResponseEntity.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.dto.ApiResponseDto;
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;
import xyz.jocn.chat.user.dto.UserUpdateRequestDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

	private final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final String JSON = MediaType.APPLICATION_JSON_VALUE;
	private final String MULTIPART_FORM = MediaType.MULTIPART_FORM_DATA_VALUE;

	private final UserService userService;

	@PostMapping(value = "/users", consumes = JSON, produces = JSON)
	public ResponseEntity<ApiResponseDto> signUp(@Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto) {
		userService.signUp(userSignUpRequestDto);
		URI uri = fromCurrentRequest().path("/me").buildAndExpand().toUri();
		return created(uri).body(success());
	}

	@GetMapping(value = "/users/me", produces = JSON)
	public ResponseEntity<ApiResponseDto> fetchMe(@AuthenticationPrincipal(expression = UID) String uid) {
		return ok(success(userService.fetchMe(Long.parseLong(uid))));
	}

	@PatchMapping(value = "/users/me", consumes = JSON, produces = JSON)
	public ResponseEntity<ApiResponseDto> updateMe(
		@Valid @RequestBody UserUpdateRequestDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		userService.updateMe(Long.parseLong(uid), dto);
		return ok(success());
	}

	@DeleteMapping(value = "/users/me", produces = JSON)
	public ResponseEntity<ApiResponseDto> exit(@AuthenticationPrincipal(expression = UID) String uid) {
		userService.exit(Long.parseLong(uid));
		return ok(success());
	}

	@PatchMapping(value = "/users/me/profile-image", consumes = MULTIPART_FORM, produces = JSON)
	public ResponseEntity<ApiResponseDto> updateProfileImg(
		MultipartFile file,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		userService.updateProfileImg(Long.parseLong(uid), file);
		return ok(success());
	}

}
