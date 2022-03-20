package xyz.jocn.chat.room;

import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.room.dto.RoomCreateRequestDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RoomController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final RoomService roomService;

	@PostMapping("/rooms")
	public ResponseEntity open(
		@AuthenticationPrincipal(expression = UID) String uid,
		@RequestBody RoomCreateRequestDto dto
	) {
		long hostId = Long.parseLong(uid);
		long inviteeId = dto.getInviteeId();
		return ok(success(roomService.open(hostId, inviteeId)));
	}

	@GetMapping("/me/rooms")
	public ResponseEntity fetchMyRooms(@AuthenticationPrincipal(expression = UID) String uid) {
		return ok(success(roomService.fetchMyRooms(Long.parseLong(uid))));
	}

}
