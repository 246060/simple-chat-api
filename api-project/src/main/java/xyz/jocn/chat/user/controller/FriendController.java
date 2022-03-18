package xyz.jocn.chat.user.controller;

import static org.springframework.http.ResponseEntity.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.user.dto.FriendBlockCancelRequestDto;
import xyz.jocn.chat.user.dto.FriendBlockCreateRequestDto;
import xyz.jocn.chat.user.dto.FriendCreateRequestDto;
import xyz.jocn.chat.user.dto.FriendDeleteRequestDto;
import xyz.jocn.chat.user.service.FriendService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/friends")
@RestController
public class FriendController {

	private static final String USER_PK = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final FriendService friendService;

	@PostMapping
	public ResponseEntity addFriend(
		@RequestBody @Valid FriendCreateRequestDto dto,
		@AuthenticationPrincipal(expression = USER_PK) String userId
	) {
		dto.setUserId(Long.parseLong(userId));
		Long id = friendService.addFriend(dto);
		return created(fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri()).body(success());
	}

	@DeleteMapping
	public ResponseEntity deleteFriends(
		@RequestBody @Valid FriendDeleteRequestDto dto,
		@AuthenticationPrincipal(expression = USER_PK) String userId
	) {
		dto.setSourceId(Long.parseLong(userId));
		friendService.deleteFriends(dto);
		return ok(success());
	}

	@PostMapping("/block")
	public ResponseEntity addBlock(@RequestBody @Valid FriendBlockCreateRequestDto dto) {
		long id = friendService.addBlock(dto);
		return created(fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri()).body(success());
	}

	@GetMapping("/block")
	public ResponseEntity fetchBlocks(@AuthenticationPrincipal(expression = USER_PK) String userId) {
		return ok(success(friendService.fetchBlocks(Long.parseLong(userId))));
	}

	@DeleteMapping("/block")
	public ResponseEntity cancelBlock(
		@RequestBody @Valid FriendBlockCancelRequestDto dto,
		@AuthenticationPrincipal(expression = USER_PK) String userId
	) {
		dto.setSourceId(Long.parseLong(userId));
		friendService.cancelBlock(dto);
		return ok(success());
	}
}
