package xyz.jocn.chat.message.controller;

import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.message.dto.ThreadMessageReactionAddRequestDto;
import xyz.jocn.chat.message.dto.ThreadMessageSendRequestDto;
import xyz.jocn.chat.message.service.ThreadMessageService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ThreadMessageController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final ThreadMessageService threadMessageService;

	@PostMapping("/threads/{threadId}/messages")
	public ResponseEntity sendThreadMessage(
		@PathVariable Long threadId,
		@RequestBody ThreadMessageSendRequestDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		threadMessageService.sendThreadMessage(Long.parseLong(uid), threadId, dto);
		return ok(success());
	}

	@GetMapping("/threads/{threadId}/messages")
	public ResponseEntity fetchMessages(
		@PathVariable Long threadId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		return ok(success(threadMessageService.fetchMessages(threadId)));
	}

	@DeleteMapping("/threads/{threadId}/messages/{messageId}")
	public ResponseEntity deleteThreadMessage(
		@PathVariable Long threadId,
		@PathVariable Long messageId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		threadMessageService.deleteThreadMessage(Long.parseLong(uid), threadId, messageId);
		return ok(success());
	}

	@PostMapping("/threads/{threadId}/messages/{messageId}/reactions")
	public ResponseEntity addMessageReaction(
		@PathVariable Long threadId,
		@PathVariable Long messageId,
		@RequestBody ThreadMessageReactionAddRequestDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		threadMessageService.addThreadMessageReaction(Long.parseLong(uid), threadId, messageId, dto);
		return ok(success());
	}

	@GetMapping("/threads/{threadId}/messages/{messageId}/reactions")
	public ResponseEntity fetchReactions(
		@PathVariable Long threadId,
		@PathVariable Long messageId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		return ok(success(threadMessageService.fetchThreadMessageReactions(threadId, messageId)));
	}

	@DeleteMapping("/threads/{threadId}/messages/{messageId}/reactions/{reactionId}")
	public ResponseEntity cancelMessageReaction(
		@PathVariable Long threadId,
		@PathVariable Long messageId,
		@PathVariable Long reactionId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		threadMessageService.cancelThreadMessageReaction(threadId, messageId, reactionId);
		return ok(success());
	}
}
