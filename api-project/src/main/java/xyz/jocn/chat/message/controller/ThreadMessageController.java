package xyz.jocn.chat.message.controller;

import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.message.dto.ThreadMessageChangeDto;
import xyz.jocn.chat.message.dto.ThreadMessageCreateDto;
import xyz.jocn.chat.message.dto.ThreadMessageMarkCreateDto;
import xyz.jocn.chat.message.service.ThreadMessageService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/threads")
@RestController
public class ThreadMessageController {

	private static final String USER_PK = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final ThreadMessageService threadMessageService;

	@PostMapping("/{threadId}/messages")
	public ResponseEntity send(
		@AuthenticationPrincipal(expression = USER_PK) String userId,
		@PathVariable Long threadId,
		@RequestBody ThreadMessageCreateDto dto
	) {
		dto.setThreadId(threadId);
		dto.setUserId(Long.parseLong(userId));
		threadMessageService.send(dto);
		return ok(success());
	}

	@GetMapping("/{threadId}/messages")
	public ResponseEntity getMessages(@PathVariable Long threadId) {
		return ok(success(threadMessageService.getMessages(threadId)));
	}

	@PatchMapping("/messages/{threadMessageId}")
	public ResponseEntity changeMessage(
		@PathVariable Long threadMessageId,
		@RequestBody ThreadMessageChangeDto dto
	) {
		dto.setThreadMessageId(threadMessageId);
		threadMessageService.change(dto);
		return ok(success());
	}

	@PostMapping("/messages/{threadMessageId}/marks")
	public ResponseEntity mark(
		@PathVariable Long threadMessageId,
		@RequestBody ThreadMessageMarkCreateDto dto
	) {

		dto.setThreadMessageId(threadMessageId);
		threadMessageService.mark(dto);
		return ok(success());
	}

	@GetMapping("/messages/{threadMessageId}/marks")
	public ResponseEntity get2(@PathVariable Long threadMessageId) {
		return ok(success(threadMessageService.getMarks(threadMessageId)));
	}

	@DeleteMapping("/messages/marks/{markId}")
	public ResponseEntity pu3t(@PathVariable Long markId) {
		threadMessageService.cancelThreadMessageMark(markId);
		return ok(success());
	}
}
