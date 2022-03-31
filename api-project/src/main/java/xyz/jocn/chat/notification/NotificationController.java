package xyz.jocn.chat.notification;

import static org.springframework.http.ResponseEntity.*;
import static xyz.jocn.chat.common.dto.ApiResponseDto.*;
import static xyz.jocn.chat.notification.enums.RoutingType.*;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.notification.dto.EventDto;
import xyz.jocn.chat.notification.dto.EvtAllAdDto;
import xyz.jocn.chat.notification.dto.EvtChannelJoinDto;
import xyz.jocn.chat.notification.dto.EvtRoutingDto;
import xyz.jocn.chat.user.dto.UserDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class NotificationController {

	private final RedisTemplate<String, String> redisTemplate;

	@PostMapping("/event.api.sever.channel")
	public ResponseEntity channel() {

		EventDto event = new EventDto();
		event.setRouting(new EvtRoutingDto(channel, String.valueOf(1000000L)));

		UserDto user = new UserDto();
		user.setId(1L);
		user.setName("hong kil dong");

		event.setMessage(new EvtChannelJoinDto(1000000L, List.of(user)));

		redisTemplate.convertAndSend("event.api.server", event);
		return ok(success());
	}

	@PostMapping("/event.api.sever.all")
	public ResponseEntity all() {
		EventDto event = new EventDto();
		event.setRouting(new EvtRoutingDto(all, null));
		event.setMessage(new EvtAllAdDto());

		redisTemplate.convertAndSend("event.api.server", event);
		return ok(success());
	}

}
