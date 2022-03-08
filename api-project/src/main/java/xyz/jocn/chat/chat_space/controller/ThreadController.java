package xyz.jocn.chat.chat_space.controller;

import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.chat_space.dto.ThreadOpenRequestDto;
import xyz.jocn.chat.chat_space.service.ThreadService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ThreadController {

	private static final String USER_PK = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final ThreadService threadService;

	@PostMapping("/rooms/messages/{messageId}/threads")
	public ResponseEntity createThread(@PathVariable Long messageId, @RequestBody ThreadOpenRequestDto dto) {
		dto.setMessageId(messageId);
		threadService.openThread(dto);
		return ok(success());
	}
}
