package xyz.jocn.chat.participant.controller;

import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.participant.service.ThreadParticipantService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ThreadParticipantController {

	private static final String USER_PK = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final ThreadParticipantService threadParticipantService;

	@PostMapping("/threads/{threadId}/participants")
	public ResponseEntity create(@PathVariable Long threadId) {
		return ok(success());
	}

	@GetMapping("/threads/{threadId}/participants")
	public ResponseEntity get(@PathVariable Long threadId) {
		return ok(success());
	}

	@DeleteMapping("/threads/participants/{threadParticipantId}")
	public ResponseEntity get2(@PathVariable Long threadParticipantId) {
		return ok(success());
	}
}
