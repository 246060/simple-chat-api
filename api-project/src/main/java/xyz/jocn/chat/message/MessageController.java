package xyz.jocn.chat.message;

import static org.springframework.http.MediaType.*;
import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.dto.SliceCriteria;
import xyz.jocn.chat.common.dto.SliceDirection;
import xyz.jocn.chat.message.dto.MessageDto;
import xyz.jocn.chat.message.dto.MessageSendRequestDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MessageController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final MessageService messageService;

	@PostMapping(
		value = "/channels/{channelId}/messages",
		consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity sendMessage(
		@PathVariable Long channelId,
		@RequestBody MessageSendRequestDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		// MvcUriComponentsBuilder.fromMethodName(RoomController.class,"getOne").build();
		messageService.sendMessage(Long.parseLong(uid), channelId, dto);
		return ok(success());
	}

	@GetMapping(
		value = "/channels/{channelId}/messages",
		consumes = APPLICATION_JSON_VALUE
	)
	public ResponseEntity fetchMessages(
		@PathVariable Long channelId,
		@RequestParam(required = false, name = "basePoint") Long messageId,
		@RequestParam(required = false, defaultValue = "up") SliceDirection direction,
		@RequestParam(required = false, defaultValue = "50") Integer limit,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		SliceCriteria<Long> criteria = new SliceCriteria<>(messageId, direction, limit);
		List<MessageDto> data = messageService.fetchMessages(Long.parseLong(uid), channelId, criteria);

		if (direction == SliceDirection.up) {
			criteria.updateNextBasePoint(data.get(data.size() - 1).getId());
		} else {
			criteria.updateNextBasePoint(data.get(0).getId());
		}

		return ok(success(data, criteria));
	}

	@DeleteMapping(
		value = "/channels/{channelId}/messages/{messageId}",
		consumes = APPLICATION_JSON_VALUE
	)
	public ResponseEntity deleteMessage(
		@PathVariable Long channelId,
		@PathVariable Long messageId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		messageService.deleteMessage(Long.parseLong(uid), channelId, messageId);
		return ok(success());
	}

	@GetMapping(
		value = "/channels/{channelId}/messages/{messageId}/read-count",
		consumes = APPLICATION_JSON_VALUE
	)
	public ResponseEntity fetchReadMessageCount(
		@PathVariable Long channelId,
		@PathVariable Long messageId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		return ok(success());
	}

}
