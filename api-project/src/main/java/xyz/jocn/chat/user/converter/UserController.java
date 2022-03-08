package xyz.jocn.chat.user.converter;

import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.user.dto.UserSignUpRequestDto;
import xyz.jocn.chat.user.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

	private static final String USER_PK = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final UserService userService;

	@PostMapping
	public ResponseEntity signUp(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
		userService.signUp(userSignUpRequestDto);
		return ResponseEntity.ok(success());
	}

	@PostMapping("/{userId}/friends")
	public ResponseEntity create(@PathVariable String userId) {
		return null;
	}

	@PostMapping("/{userId}/friends/groups")
	public ResponseEntity group() {
		return null;
	}

	@PostMapping("/{userId}/friends/groups/{groupId}")
	public ResponseEntity group(@PathVariable String groupId) {
		return null;
	}

	@PostMapping("/{userId}/friends/block")
	public ResponseEntity blockFriendAddition(@PathVariable String userId) {
		return null;
	}
}
