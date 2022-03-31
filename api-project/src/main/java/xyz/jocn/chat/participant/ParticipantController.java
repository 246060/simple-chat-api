package xyz.jocn.chat.participant;

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
import xyz.jocn.chat.participant.dto.ChannelExitDto;
import xyz.jocn.chat.participant.dto.ChannelInviteRequestDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ParticipantController {

	private static final String UID = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final ParticipantService participantService;

	@PostMapping("/channels/{channelId}/participants")
	public ResponseEntity invite(
		@PathVariable Long channelId,
		@RequestBody ChannelInviteRequestDto dto,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		participantService.invite(Long.parseLong(uid), channelId, dto);
		return ok(success());
	}

	@GetMapping("/channels/{channelId}/participants")
	public ResponseEntity fetchCurrentParticipantsInChannel(
		@PathVariable Long channelId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		return ok(success(participantService.fetchCurrentParticipantsInChannel(Long.parseLong(uid), channelId)));
	}

	@GetMapping("/channels/{channelId}/participants/{participantId}")
	public ResponseEntity fetchOneInChannel(
		@PathVariable Long channelId,
		@PathVariable Long participantId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		return ok(success(participantService.fetchOneInChannel(Long.parseLong(uid), channelId, participantId)));
	}

	@DeleteMapping("/channels/{channelId}/participants/{participantId}")
	public ResponseEntity exit(
		@PathVariable Long channelId,
		@PathVariable Long participantId,
		@AuthenticationPrincipal(expression = UID) String uid
	) {
		ChannelExitDto dto = new ChannelExitDto();
		dto.setChannelId(channelId);
		dto.setParticipantId(participantId);
		dto.setUserId(Long.parseLong(uid));

		participantService.exit(dto);
		return ok(success());
	}
}
