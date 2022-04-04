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
import xyz.jocn.chat.notification.dto.EventContext;
import xyz.jocn.chat.notification.dto.EventDto;
import xyz.jocn.chat.notification.dto.EvtAllAd;
import xyz.jocn.chat.notification.dto.EvtChannelJoin;
import xyz.jocn.chat.notification.dto.EventRouting;
import xyz.jocn.chat.user.dto.UserDto;

@Slf4j
@RequiredArgsConstructor
@RestController
public class NotificationController {

	private final RedisTemplate<String, String> redisTemplate;

	@PostMapping("/event.api.sever.channel")
	public ResponseEntity channel() {

		EventRouting routing = new EventRouting(channel, String.valueOf(1000000L));

		UserDto user = new UserDto();
		user.setId(1L);
		user.setName("hong kil dong");
		EvtChannelJoin data = new EvtChannelJoin(1000000L, List.of(user));

		redisTemplate.convertAndSend("event.api.server", new EventContext(routing, data));

		return ok(success());
	}

	@PostMapping("/event.api.sever.all")
	public ResponseEntity all() {

		EventRouting routing = new EventRouting(all, null);
		EvtAllAd data = new EvtAllAd();

		redisTemplate.convertAndSend("event.api.server", new EventContext(routing, data));

		return ok(success());
	}

}
