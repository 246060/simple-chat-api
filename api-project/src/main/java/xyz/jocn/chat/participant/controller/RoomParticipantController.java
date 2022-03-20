package xyz.jocn.chat.participant.controller;

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
import xyz.jocn.chat.participant.dto.RoomExitRequestDto;
import xyz.jocn.chat.participant.dto.RoomInviteRequestDto;
import xyz.jocn.chat.participant.service.RoomParticipantService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RoomParticipantController {
	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final RoomParticipantService roomParticipantService;

	@PostMapping("/rooms/{roomId}/participants")
	public ResponseEntity invite(
		@PathVariable Long roomId,
		@RequestBody RoomInviteRequestDto dto
	) {
		dto.setRoomId(roomId);
		roomParticipantService.invite(dto);
		return ok(success());
	}

	@GetMapping("/rooms/{roomId}/participants")
	public ResponseEntity fetchParticipants(@PathVariable Long roomId) {
		return ok(success(roomParticipantService.fetchParticipants(roomId)));
	}

	@DeleteMapping("/rooms/participants/{participantId}")
	public ResponseEntity exit(
		@PathVariable Long participantId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		RoomExitRequestDto dto = new RoomExitRequestDto();
		dto.setParticipantId(participantId);
		dto.setUserId(Long.parseLong(uid));

		roomParticipantService.exit(dto);
		return ok(success());
	}
}
