package xyz.jocn.chat.thread;

import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.thread.dto.ThreadOpenDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ThreadController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final ThreadService threadService;

	@PostMapping("/rooms/messages/{roomMessageId}/thread")
	public ResponseEntity open(
		@PathVariable Long roomMessageId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		ThreadOpenDto dto = new ThreadOpenDto();
		dto.setRoomMessageId(roomMessageId);
		dto.setUserId(Long.parseLong(uid));
		return ok(success(threadService.open(dto)));
	}

	@GetMapping("/me/threads")
	public ResponseEntity fetchMyThreads(@AuthenticationPrincipal(expression = UID) String uid) {
		return ok(success(threadService.fetchMyThreads(Long.parseLong(uid))));
	}
}
