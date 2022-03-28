package xyz.jocn.chat.participant.controller;

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
import xyz.jocn.chat.participant.dto.ThreadJoinDto;
import xyz.jocn.chat.participant.service.ThreadParticipantService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ThreadParticipantController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final ThreadParticipantService threadParticipantService;

	@PostMapping("/rooms/{roomId}/threads/{threadId}/participants")
	public ResponseEntity join(
		@PathVariable Long roomId,
		@PathVariable Long threadId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		ThreadJoinDto dto = new ThreadJoinDto();
		dto.setRoomId(roomId);
		dto.setThreadId(threadId);
		dto.setUserId(Long.parseLong(uid));
		threadParticipantService.join(dto);
		return ok(success());
	}

	@GetMapping("/rooms/{roomId}/threads/{threadId}/participants")
	public ResponseEntity fetchThreadParticipants(@PathVariable Long roomId, @PathVariable Long threadId) {
		return ok(success(threadParticipantService.fetchThreadParticipants(roomId, threadId)));
	}

}
