package xyz.jocn.chat.room;

import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.room.dto.RoomCreateRequestDto;
import xyz.jocn.chat.room.dto.RoomInviteRequestDto;
import xyz.jocn.chat.room.dto.RoomMessageGetRequestDto;
import xyz.jocn.chat.room.dto.RoomMessageMarkCreateRequestDto;
import xyz.jocn.chat.room.dto.RoomMessageSendRequestDto;
import xyz.jocn.chat.room.dto.ThreadMessageCreateRequestDto;
import xyz.jocn.chat.room.dto.ThreadMessageMarkCreateRequestDto;
import xyz.jocn.chat.room.dto.ThreadOpenRequestDto;
import xyz.jocn.chat.room.service.RoomService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/rooms")
@RestController
public class RoomController {

	private static final String USER_PK = "subject";
	private final RoomService roomService;

	@PostMapping
	public ResponseEntity open(
		@AuthenticationPrincipal(expression = USER_PK) String userId,
		@RequestBody RoomCreateRequestDto dto
	) {
		dto.setHostId(userId);
		return ok(success(roomService.open(dto)));
	}

	@GetMapping
	public ResponseEntity<?> getRooms(
		@AuthenticationPrincipal(expression = USER_PK) String userId
	) {
		return ok(success(roomService.getRoomList(userId)));
	}

	@PostMapping("/{roomId}/participants")
	public ResponseEntity invite(
		@PathVariable Long roomId,
		@AuthenticationPrincipal(expression = USER_PK) String userId,
		@RequestBody RoomInviteRequestDto dto
	) {
		dto.setRoomId(roomId);
		roomService.invite(dto);
		return ok(success());
	}

	@GetMapping("/{roomId}/participants")
	public ResponseEntity getParticipants(
		@PathVariable Long roomId,
		@AuthenticationPrincipal(expression = USER_PK) String userId
	) {
		return ok(success(roomService.getParticipants(roomId)));
	}

	@DeleteMapping("/{roomId}/participants/{participantId}")
	public ResponseEntity exit(
		@PathVariable Long roomId,
		@PathVariable Long participantId,
		@AuthenticationPrincipal(expression = USER_PK) String userId
	) {
		roomService.exit(participantId);
		return ok(success());
	}

	@PostMapping("/{roomId}/messages")
	public ResponseEntity create(
		@PathVariable Long roomId,
		@AuthenticationPrincipal(expression = USER_PK) String userId,
		@RequestBody RoomMessageSendRequestDto dto
	) {
		// MvcUriComponentsBuilder.fromMethodName(RoomController.class,"getOne").build();
		dto.setUserId(Long.parseLong(userId));
		dto.setRoomId(roomId);
		roomService.sendMessageToRoom(dto);
		return ok(success());
	}

	@GetMapping("/{roomId}/messages")
	public ResponseEntity<?> getRoomMessages(
		@PathVariable Long roomId,
		@RequestBody RoomMessageGetRequestDto dto
	) {
		dto.setRoomId(roomId);
		return ok(success(roomService.getMessagesInRoom(dto)));
	}

	@PostMapping("/{roomId}/messages/{messageId}/marks")
	public ResponseEntity post(
		@PathVariable Long roomId,
		@PathVariable Long messageId,
		@AuthenticationPrincipal(expression = USER_PK) String userId,
		@RequestBody RoomMessageMarkCreateRequestDto dto
	) {
		dto.setMessageId(messageId);
		dto.setUserId(Long.parseLong(userId));
		roomService.putMarkOnRoomMessage(dto);
		return ok(success());
	}

	@GetMapping("/{roomId}/messages/{messageId}/marks")
	public ResponseEntity<?> get(
		@PathVariable Long roomId,
		@PathVariable Long messageId
	) {
		return ok(success(roomService.getMessageMarks(messageId)));
	}

	@DeleteMapping("/{roomId}/messages/{messageId}/marks/{markId}")
	public ResponseEntity<?> put(
		@PathVariable String roomId,
		@PathVariable String messageId,
		@PathVariable Long markId
	) {
		roomService.cancelRoomMessageMark(markId);
		return ok(success());
	}

	@PostMapping("/{roomId}/messages/{messageId}/threads")
	public ResponseEntity createThread(
		@PathVariable Long roomId,
		@PathVariable Long messageId,
		@RequestBody ThreadOpenRequestDto dto
	) {
		dto.setMessageId(messageId);
		roomService.openThread(dto);
		return ok(success());
	}

	@PostMapping("/{roomId}/messages/{messageId}/threads/{threadId}/messages")
	public ResponseEntity createThreadMessage(
		@AuthenticationPrincipal(expression = USER_PK) String userId,
		@RequestBody ThreadMessageCreateRequestDto dto
	) {
		roomService.sendMessageToThread(dto);
		return ok(success());
	}

	@GetMapping("/{roomId}/messages/{messageId}/threads/{threadId}/messages")
	public ResponseEntity getThreadMessage(
		@PathVariable Long threadId,
		@AuthenticationPrincipal(expression = USER_PK) String userId
	) {
		return ok(success(roomService.getMessagesInThread(threadId)));
	}

	@PostMapping("/{roomId}/messages/{roomMessageId}/threads/{threadId}/messages/{threadMessageId}/marks")
	public ResponseEntity post1(
		@PathVariable Long threadMessageId,
		@AuthenticationPrincipal(expression = USER_PK) String userId,
		@RequestBody ThreadMessageMarkCreateRequestDto dto
	) {
		dto.setThreadMessageId(threadMessageId);
		roomService.putMarkOnThreadMessage(dto);
		return ok(success());
	}

	@GetMapping("/{roomId}/messages/{roomMessageId}/threads/{threadId}/messages/{threadMessageId}/marks")
	public ResponseEntity<?> get2(
		@PathVariable Long threadMessageId
	) {
		return ok(success(roomService.getThreadMessageMarks(threadMessageId)));
	}

	@DeleteMapping("/{roomId}/messages/{roomMessageId}/threads/{threadId}/messages/{threadMessageId}/marks/{markId}")
	public ResponseEntity<?> pu3t(
		@PathVariable Long markId
	) {
		roomService.cancelThreadMessageMark(markId);
		return ok(success());
	}
}
