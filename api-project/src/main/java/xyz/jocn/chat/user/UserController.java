package xyz.jocn.chat.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

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
