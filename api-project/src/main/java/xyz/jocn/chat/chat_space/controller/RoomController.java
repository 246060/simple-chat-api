package xyz.jocn.chat.chat_space.controller;

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
import xyz.jocn.chat.chat_space.dto.RoomCreateDto;
import xyz.jocn.chat.chat_space.service.RoomService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RoomController {

	private static final String USER_PK = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final RoomService roomService;

	@PostMapping("/rooms")
	public ResponseEntity open(
		@AuthenticationPrincipal(expression = USER_PK) String userId,
		@RequestBody RoomCreateDto dto
	) {
		dto.setHostId(userId);
		return ok(success(roomService.open(dto)));
	}

	@GetMapping("/rooms")
	public ResponseEntity getRooms(@AuthenticationPrincipal(expression = USER_PK) String userId) {
		return ok(success(roomService.getRoomList(Long.parseLong(userId))));
	}

}
