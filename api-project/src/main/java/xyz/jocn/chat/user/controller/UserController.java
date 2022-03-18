package xyz.jocn.chat.user.controller;

import static org.springframework.http.ResponseEntity.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;
import static xyz.jocn.chat.common.util.AppUtil.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.exception.ApiAccessDenyException;
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;
import xyz.jocn.chat.user.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final UserService userService;

	@PostMapping
	public ResponseEntity<?> signUp(@RequestBody @Valid UserSignUpRequestDto userSignUpRequestDto) {
		long id = userService.signUp(userSignUpRequestDto);
		return created(fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri()).body(success());
	}

	@GetMapping("/me")
	public ResponseEntity<?> fetchMe(@AuthenticationPrincipal(expression = UID) String userId) {
		return ok(success(userService.fetchMe(Long.parseLong(userId))));
	}

	@DeleteMapping("/me")
	public ResponseEntity<?> exit(@AuthenticationPrincipal(expression = UID) String userId) {
		userService.exit(Long.parseLong(userId));
		return ok(success());
	}
}
