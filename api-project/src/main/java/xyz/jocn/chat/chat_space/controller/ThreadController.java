package xyz.jocn.chat.chat_space.controller;

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
import xyz.jocn.chat.chat_space.dto.ThreadOpenDto;
import xyz.jocn.chat.chat_space.service.ThreadService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ThreadController {

	private static final String USER_PK = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final ThreadService threadService;

	@PostMapping("/rooms/messages/{roomMessageId}/threads")
	public ResponseEntity open(
		@PathVariable Long roomMessageId,
		@AuthenticationPrincipal(expression = USER_PK) String userId
	) {
		ThreadOpenDto dto = new ThreadOpenDto();
		dto.setRoomMessageId(roomMessageId);
		dto.setUserId(Long.parseLong(userId));

		return ok(success(threadService.open(dto)));
	}

	@GetMapping("/threads")
	public ResponseEntity getRooms(@AuthenticationPrincipal(expression = USER_PK) String userId) {
		return ok(success(threadService.getThreads(Long.parseLong(userId))));
	}
}
