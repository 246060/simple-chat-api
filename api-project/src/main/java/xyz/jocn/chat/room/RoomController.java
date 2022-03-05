package xyz.jocn.chat.room;

import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import java.net.URI;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/rooms")
@RestController
public class RoomController {

	@PostMapping
	public ResponseEntity create() {
		return null;
	}

	@GetMapping
	public ResponseEntity<?> getRooms() {
		return null;
	}

	@PostMapping("/{roomId}/participants")
	public ResponseEntity invite(@PathVariable String roomId) {
		return null;
	}

	@GetMapping("/{roomId}/participants")
	public ResponseEntity getParticipants(@PathVariable String roomId) {
		return null;
	}

	@DeleteMapping("/{roomId}/participants/{participantId}")
	public ResponseEntity exit(@PathVariable String roomId, @PathVariable String participantId) {
		return null;
	}

	@PostMapping("/{roomId}/messages")
	public ResponseEntity create(@PathVariable String roomId) {
		// MvcUriComponentsBuilder.fromMethodName(RoomController.class,"getOne").build();
		// 1. 체크 : 메시지 송신자가 룸의 파티원인가?
		// 2. 메시지 저장
		// 3. 룸 메시지 event publish
		return null;
	}

	@GetMapping("/{roomId}/messages")
	public ResponseEntity<?> getRoomMessages(@PathVariable String roomId) {
		return null;
	}

	@PostMapping("/{roomId}/messages/{messageId}/marks")
	public ResponseEntity post(
		@PathVariable String roomId,
		@PathVariable String messageId
	) {
		return null;
	}

	@GetMapping("/{roomId}/messages/{messageId}/marks")
	public ResponseEntity<?> get(
		@PathVariable String roomId,
		@PathVariable String messageId
	) {
		return null;
	}

	@PatchMapping("/{roomId}/messages/{messageId}/marks/{markId}")
	public ResponseEntity<?> put(
		@PathVariable String roomId,
		@PathVariable String messageId,
		@PathVariable String markId
	) {
		return null;
	}


	@PostMapping("/{roomId}/messages/{messageId}/threads")
	public ResponseEntity createThread(
		@PathVariable String roomId,
		@PathVariable String messageId
	) {
		return null;
	}

	@PostMapping("/{roomId}/messages/{messageId}/threads/{threadId}/messages")
	public ResponseEntity createThreadMessage(
		@PathVariable String roomId,
		@PathVariable String messageId,
		@PathVariable String threadId
	) {
		return null;
	}

	@GetMapping("/{roomId}/messages/{messageId}/threads/{threadId}/messages")
	public ResponseEntity getThreadMessage(
		@PathVariable String roomId,
		@PathVariable String messageId,
		@PathVariable String threadId
	) {
		return null;
	}
}
