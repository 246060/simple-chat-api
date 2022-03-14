package xyz.jocn.chat.user;

import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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

@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
@RestController
public class UserController {

	private static final String USER_PK = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final UserService userService;

	@PostMapping
	public ResponseEntity signUp(@RequestBody @Valid UserSignUpRequestDto userSignUpRequestDto) {
		userService.signUp(userSignUpRequestDto);
		return ok(success());
	}

	@GetMapping("/me")
	public ResponseEntity me(@AuthenticationPrincipal(expression = USER_PK) String userId) {
		return ok(success(userService.getUser(Long.parseLong(userId))));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity withdrawal(
		@PathVariable Long id,
		@AuthenticationPrincipal(expression = USER_PK) String userId
	) {
		if (userService.isNotResourceOwner(id, Long.parseLong(userId))) {
			throw new ApiAccessDenyException("only withdraw own account");
		}
		userService.withdrawal(id);
		return ok(success());
	}
}
