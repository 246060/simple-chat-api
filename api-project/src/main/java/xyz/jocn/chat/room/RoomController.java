package xyz.jocn.chat.room;

import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.AppConstants.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.pubsub.EventDto;
import xyz.jocn.chat.common.pubsub.EventTarget;
import xyz.jocn.chat.common.pubsub.EventType;
import xyz.jocn.chat.common.pubsub.MessagePublisher;
import xyz.jocn.chat.room.dto.RoomCreateDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RoomController {

	private static final String USER_PK = JWT_CLAIM_FIELD_NAME_USER_KEY;
	private final RoomService roomService;

	private final MessagePublisher redisMessagePublisher;

	// @PostConstruct
	public void init() throws InterruptedException {

		for (int i = 0; i < 100; i++) {
			CompletableFuture.runAsync(() -> {
				try {
					Thread.sleep(new Random().nextInt(10000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				redisMessagePublisher.emit(
					EventDto.builder()
						.id(new Random().nextLong())
						.message("helloworld")
						.receiver(List.of(new Random().nextLong()))
						.spaceId(new Random().nextLong())
						.type(EventType.ROOM_EVENT)
						.target(EventTarget.ROOM_AREA)
						.build()
				);
			});
		}
	}

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
