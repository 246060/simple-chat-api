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
import xyz.jocn.chat.thread.dto.ThreadOpenRequestDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ThreadController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final ThreadService threadService;

	@PostMapping("/rooms/{roomId}/messages/{messageId}/thread")
	public ResponseEntity open(
		@PathVariable Long roomId,
		@PathVariable Long messageId,
		@PathVariable Long roomMessageId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		ThreadOpenRequestDto dto = new ThreadOpenRequestDto();
		dto.setRoomMessageId(roomMessageId);
		dto.setUserId(Long.parseLong(uid));
		dto.setRoomId(roomId);
		dto.setMessageId(messageId);

		return ok(success(threadService.open(dto)));
	}

	@GetMapping("/threads")
	public ResponseEntity fetchMyThreads(@AuthenticationPrincipal(expression = UID) String uid) {
		return ok(success(threadService.fetchMyThreads(Long.parseLong(uid))));
	}
}
