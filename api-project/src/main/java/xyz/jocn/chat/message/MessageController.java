package xyz.jocn.chat.message;

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
import xyz.jocn.chat.message.dto.MessageSendRequestDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MessageController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final MessageService messageService;

	@PostMapping("/channels/{roomId}/messages")
	public ResponseEntity sendMessage(
		@PathVariable Long channelId,
		@RequestBody MessageSendRequestDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		// MvcUriComponentsBuilder.fromMethodName(RoomController.class,"getOne").build();
		messageService.sendMessage(Long.parseLong(uid), channelId, dto);
		return ok(success());
	}

	@GetMapping("/channels/{channelId}/messages")
	public ResponseEntity fetchRoomMessages(
		@PathVariable Long channelId,
		PageMeta pageMeta,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		return ok(success(messageService.fetchMessages(Long.parseLong(uid), channelId, pageMeta)));
	}

	@DeleteMapping("/channels/{channelId}/messages/{messageId}")
	public ResponseEntity deleteRoomMessage(
		@PathVariable Long channelId,
		@PathVariable Long messageId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		messageService.deleteMessage(Long.parseLong(uid), channelId, messageId);
		return ok(success());
	}

}
