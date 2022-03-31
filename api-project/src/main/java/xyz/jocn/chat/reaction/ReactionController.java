package xyz.jocn.chat.reaction;

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
import xyz.jocn.chat.reaction.dto.ReactionAddRequestDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReactionController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final ReactionService reactionService;

	@PostMapping("/channels/{channelId}/messages/{messageId}/reactions")
	public ResponseEntity addReaction(
		@PathVariable Long channelId,
		@PathVariable Long messageId,
		@RequestBody ReactionAddRequestDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		reactionService.addReaction(Long.parseLong(uid), channelId, messageId, dto);
		return ok(success());
	}

	@GetMapping("/channels/{channelId}/messages/{messageId}/reactions")
	public ResponseEntity fetchReactions(
		@PathVariable Long channelId,
		@PathVariable Long messageId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		return ok(success(reactionService.fetchReactions(Long.parseLong(uid), channelId, messageId)));
	}

	@DeleteMapping("/channels/{channelId}/messages/{messageId}/reactions/{reactionId}")
	public ResponseEntity cancelReaction(
		@PathVariable Long channelId,
		@PathVariable Long messageId,
		@PathVariable Long reactionId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		reactionService.cancelReaction(Long.parseLong(uid), channelId, messageId, reactionId);
		return ok(success());
	}
}
