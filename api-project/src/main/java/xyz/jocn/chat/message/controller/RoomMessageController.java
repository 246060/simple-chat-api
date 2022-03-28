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
import xyz.jocn.chat.common.dto.PageMeta;
import xyz.jocn.chat.message.dto.RoomMessageReactionAddRequestDto;
import xyz.jocn.chat.message.dto.RoomMessageSendRequestDto;
import xyz.jocn.chat.message.service.RoomMessageService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RoomMessageController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final RoomMessageService roomMessageService;

	@PostMapping("/rooms/{roomId}/messages")
	public ResponseEntity sendMessage(
		@PathVariable Long roomId,
		@RequestBody RoomMessageSendRequestDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		// MvcUriComponentsBuilder.fromMethodName(RoomController.class,"getOne").build();
		roomMessageService.sendRoomMessage(Long.parseLong(uid), roomId, dto);
		return ok(success());
	}

	@GetMapping("/rooms/{roomId}/messages")
	public ResponseEntity fetchRoomMessages(
		@PathVariable Long roomId,
		PageMeta pageMeta,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		return ok(success(roomMessageService.fetchRoomMessages(Long.parseLong(uid), roomId, pageMeta)));
	}

	@DeleteMapping("/rooms/{roomId}/messages/{messageId}")
	public ResponseEntity deleteRoomMessage(
		@PathVariable Long roomId,
		@PathVariable Long messageId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		roomMessageService.deleteRoomMessage(Long.parseLong(uid), roomId, messageId);
		return ok(success());
	}

	@PostMapping("/rooms/{roomId}/messages/{messageId}/reactions")
	public ResponseEntity addReaction(
		@PathVariable Long roomId,
		@PathVariable Long messageId,
		@RequestBody RoomMessageReactionAddRequestDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		roomMessageService.addReaction(Long.parseLong(uid), roomId, messageId, dto);
		return ok(success());
	}

	@GetMapping("/rooms/{roomId}/messages/{messageId}/reactions")
	public ResponseEntity fetchReactions(
		@PathVariable Long roomId,
		@PathVariable Long messageId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		return ok(success(roomMessageService.fetchReactions(Long.parseLong(uid), roomId, messageId)));
	}

	@DeleteMapping("/rooms/{roomId}/messages/{messageId}/reactions/{reactionId}")
	public ResponseEntity cancelReaction(
		@PathVariable Long roomId,
		@PathVariable Long messageId,
		@PathVariable Long reactionId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		roomMessageService.cancelReaction(Long.parseLong(uid), roomId, reactionId);
		return ok(success());
	}

}
