package xyz.jocn.chat.reaction;

import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import javax.validation.Valid;

import org.springframework.http.MediaType;
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
import xyz.jocn.chat.common.dto.ApiResponseDto;
import xyz.jocn.chat.reaction.dto.ReactionAddRequestDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReactionController {

	private final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final String JSON = MediaType.APPLICATION_JSON_VALUE;

	private final ReactionService reactionService;

	@PostMapping(value = "/channels/{channelId}/messages/{messageId}/reactions", consumes = JSON, produces = JSON)
	public ResponseEntity<ApiResponseDto> addReaction(
		@PathVariable Long channelId,
		@PathVariable Long messageId,
		@Valid @RequestBody ReactionAddRequestDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		reactionService.addReaction(Long.parseLong(uid), channelId, messageId, dto);
		return ok(success());
	}

	@GetMapping(value = "/channels/{channelId}/messages/{messageId}/reactions", produces = JSON)
	public ResponseEntity<ApiResponseDto> fetchReactions(
		@PathVariable Long channelId,
		@PathVariable Long messageId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		return ok(success(reactionService.fetchReactions(Long.parseLong(uid), channelId, messageId)));
	}

	@DeleteMapping(value = "/channels/{channelId}/messages/{messageId}/reactions/{reactionId}", produces = JSON)
	public ResponseEntity<ApiResponseDto> cancelReaction(
		@PathVariable Long channelId,
		@PathVariable Long messageId,
		@PathVariable Long reactionId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		reactionService.cancelReaction(Long.parseLong(uid), channelId, messageId, reactionId);
		return ok(success());
	}
}
