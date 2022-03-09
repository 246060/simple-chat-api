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
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.dto.PageMeta;
import xyz.jocn.chat.message.dto.RoomMessageChangeDto;
import xyz.jocn.chat.message.dto.RoomMessageGetDto;
import xyz.jocn.chat.message.dto.RoomMessageMarkCreateDto;
import xyz.jocn.chat.message.dto.RoomMessageSendDto;
import xyz.jocn.chat.message.service.RoomMessageService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RoomMessageController {

	private static final String USER_PK = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final RoomMessageService roomMessageService;

	@PostMapping("/rooms/{roomId}/messages")
	public ResponseEntity send(
		@PathVariable Long roomId,
		@RequestBody RoomMessageSendDto dto,
		@AuthenticationPrincipal(expression = USER_PK) String userId
	) {
		// MvcUriComponentsBuilder.fromMethodName(RoomController.class,"getOne").build();
		dto.setUserId(Long.parseLong(userId));
		dto.setRoomId(roomId);
		roomMessageService.sendMessageToRoom(dto);
		return ok(success());
	}

	@GetMapping("/rooms/{roomId}/messages")
	public ResponseEntity getMessages(
		@PathVariable Long roomId,
		PageMeta pageMeta,
		@RequestBody RoomMessageGetDto dto
	) {
		dto.setRoomId(roomId);
		return ok(success(roomMessageService.getMessagesInRoom(dto, pageMeta)));
	}

	@PatchMapping("/rooms/messages/{messageId}")
	public ResponseEntity changeMessage(@PathVariable Long messageId, @RequestBody RoomMessageChangeDto dto) {
		dto.setMessageId(messageId);
		roomMessageService.change(dto);
		return ok(success());
	}

	@PostMapping("/rooms/messages/{messageId}/marks")
	public ResponseEntity mark(
		@PathVariable Long messageId,
		@AuthenticationPrincipal(expression = USER_PK) String userId,
		@RequestBody RoomMessageMarkCreateDto dto
	) {
		dto.setMessageId(messageId);
		dto.setUserId(Long.parseLong(userId));
		roomMessageService.mark(dto);
		return ok(success());
	}

	@GetMapping("/rooms/messages/{messageId}/marks")
	public ResponseEntity getMarks(@PathVariable Long messageId) {
		return ok(success(roomMessageService.getMarks(messageId)));
	}

	@DeleteMapping("/rooms/messages/marks/{markId}")
	public ResponseEntity deleteMark(@PathVariable String messageId, @PathVariable Long markId) {
		roomMessageService.cancelMark(markId);
		return ok(success());
	}
}
