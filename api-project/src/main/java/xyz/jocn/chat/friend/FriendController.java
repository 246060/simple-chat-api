package xyz.jocn.chat.friend;

import static org.springframework.http.ResponseEntity.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.friend.dto.FriendDto;
import xyz.jocn.chat.friend.dto.FriendRequestDto;
import xyz.jocn.chat.friend.dto.FriendSearchDto;
import xyz.jocn.chat.friend.dto.FriendUidsDto;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping
@RestController
public class FriendController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final FriendService friendService;

	@PostMapping("/friends")
	public ResponseEntity addFriend(
		@RequestBody @Valid FriendRequestDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		Long id = friendService.addFriend(Long.parseLong(uid), dto);
		return created(fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri()).body(success());
	}

	@GetMapping("/friends/{friendId}")
	public ResponseEntity fetchOne(
		@PathVariable Long friendId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		return ok(success(friendService.fetchOne(Long.parseLong(uid),friendId)));
	}

	@GetMapping("/friends")
	public ResponseEntity fetchFriends(
		@RequestParam(required = false, defaultValue = "false") Boolean hidden,
		@RequestParam(required = false, defaultValue = "false") Boolean favorite,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		FriendSearchDto dto = new FriendSearchDto();
		dto.setFavorite(favorite);
		dto.setHidden(hidden);
		return ok(success(friendService.fetchFriends(Long.parseLong(uid), dto)));
	}

	@PatchMapping("/friends/{id}")
	public ResponseEntity updateFriend(
		@RequestBody @Valid FriendDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		return ok(success(friendService.updateFriend(Long.parseLong(uid), dto)));
	}

	@DeleteMapping("/friends")
	public ResponseEntity deleteFriends(
		@RequestBody @Valid FriendUidsDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		friendService.deleteFriends(Long.parseLong(uid), dto);
		return ok(success());
	}

	@PostMapping("/friends/blocks")
	public ResponseEntity addBlock(
		@RequestBody @Valid FriendRequestDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		friendService.addBlock(Long.parseLong(uid), dto);
		return ok(success());
	}

	@GetMapping("/friends/blocks")
	public ResponseEntity fetchBlocks(@AuthenticationPrincipal(expression = UID) String uid) {
		return ok(success(friendService.fetchBlocks(Long.parseLong(uid))));
	}

	@DeleteMapping("/friends/blocks")
	public ResponseEntity cancelBlock(
		@RequestBody @Valid FriendUidsDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		friendService.cancelBlock(Long.parseLong(uid), dto);
		return ok(success());
	}

}
