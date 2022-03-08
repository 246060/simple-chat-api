package xyz.jocn.chat.message.controller;

import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.message.dto.ThreadMessageCreateDto;
import xyz.jocn.chat.message.dto.ThreadMessageMarkCreateDto;
import xyz.jocn.chat.message.service.ThreadMessageService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ThreadMessageController {

	private static final String USER_PK = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final ThreadMessageService threadMessageService;

	@PostMapping("/threads/{threadId}/messages")
	public ResponseEntity createThreadMessage(@RequestBody ThreadMessageCreateDto dto) {
		threadMessageService.sendMessageToThread(dto);
		return ok(success());
	}

	@GetMapping("/threads/{threadId}/messages")
	public ResponseEntity getThreadMessage(@PathVariable Long threadId) {
		return ok(success(threadMessageService.getMessagesInThread(threadId)));
	}

	@PostMapping("/threads/messages/{threadMessageId}/marks")
	public ResponseEntity post1(@PathVariable Long threadMessageId, @RequestBody ThreadMessageMarkCreateDto dto) {
		dto.setThreadMessageId(threadMessageId);
		threadMessageService.putMarkOnThreadMessage(dto);
		return ok(success());
	}

	@GetMapping("/threads/messages/{threadMessageId}/marks")
	public ResponseEntity get2(@PathVariable Long threadMessageId) {
		return ok(success(threadMessageService.getThreadMessageMarks(threadMessageId)));
	}

	@DeleteMapping("/threads/messages/marks/{markId}")
	public ResponseEntity pu3t(@PathVariable Long markId) {
		threadMessageService.cancelThreadMessageMark(markId);
		return ok(success());
	}
}
