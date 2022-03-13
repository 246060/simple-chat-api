package xyz.jocn.chat.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FriendController {

	@PostMapping("/users/{userId}/friends")
	public ResponseEntity create(@PathVariable String userId) {
		return null;
	}

	@PostMapping("/users/{userId}/friends/groups")
	public ResponseEntity group() {
		return null;
	}

	@PostMapping("/users/{userId}/friends/groups/{groupId}")
	public ResponseEntity group(@PathVariable String groupId) {
		return null;
	}

	@PostMapping("/users/{userId}/friends/block")
	public ResponseEntity blockFriendAddition(@PathVariable String userId) {
		return null;
	}
}
