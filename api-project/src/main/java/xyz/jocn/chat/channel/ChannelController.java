package xyz.jocn.chat.channel;

import static org.springframework.http.ResponseEntity.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.channel.dto.ChannelOpenRequestDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChannelController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final ChannelService channelService;

	@PostMapping("/channels")
	public ResponseEntity open(
		@RequestBody ChannelOpenRequestDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		Long id = channelService.open(Long.parseLong(uid), dto.getInviteeId());
		URI uri = fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
		return created(uri).body(success());
	}

	@GetMapping("/channels")
	public ResponseEntity fetchMyChannels(@AuthenticationPrincipal(expression = UID) String uid) {
		return ok(success(channelService.fetchMyChannels(Long.parseLong(uid))));
	}

	@GetMapping("/channels/{channelId}")
	public ResponseEntity fetchMyChannels(
		@PathVariable Long channelId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		return ok(success(channelService.fetchMyChannel(Long.parseLong(uid), channelId)));
	}
}
