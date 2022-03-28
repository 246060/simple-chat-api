package xyz.jocn.chat.user;

import static org.springframework.http.MediaType.*;
import static org.springframework.http.ResponseEntity.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import java.net.URI;

import javax.validation.Valid;

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
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;
import xyz.jocn.chat.user.dto.UserUpdateRequestDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final UserService userService;

	@PostMapping("/users")
	public ResponseEntity signUp(@RequestBody @Valid UserSignUpRequestDto userSignUpRequestDto) {
		userService.signUp(userSignUpRequestDto);
		URI uri = fromCurrentRequest().path("/me").buildAndExpand().toUri();
		return created(uri).body(success());
	}

	@GetMapping("/users")
	public ResponseEntity fetchUsers(@AuthenticationPrincipal(expression = UID) String uid) {
		return ok(success(userService.fetchUsers()));
	}

	@GetMapping("/users/me")
	public ResponseEntity fetchMe(@AuthenticationPrincipal(expression = UID) String uid) {
		return ok(success(userService.fetchMe(Long.parseLong(uid))));
	}

	@PatchMapping("/users/me")
	public ResponseEntity updateMe(UserUpdateRequestDto dto, @AuthenticationPrincipal(expression = UID) String uid) {
		return ok(success(userService.updateMe(Long.parseLong(uid), dto)));
	}

	@DeleteMapping("/users/me")
	public ResponseEntity exit(@AuthenticationPrincipal(expression = UID) String uid) {
		userService.exit(Long.parseLong(uid));
		return ok(success());
	}

	@PatchMapping(value = "/users/me/profile-image", consumes = MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity updateProfileImg(MultipartFile file, @AuthenticationPrincipal(expression = UID) String uid) {
		userService.updateProfileImg(Long.parseLong(uid), file);
		return ok(success());
	}

}
