package xyz.jocn.chat.common.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.common.pubsub.MessagePublisher;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppMainBeanConfig {

	private final Environment environment;
	private final MessagePublisher redisMessagePublisher;

	@PostConstruct
	public void init() {

		for (String defaultProfile : environment.getDefaultProfiles()) {
			log.info("Default Profile = {}", defaultProfile);
		}
		for (String activeProfile : environment.getActiveProfiles()) {
			log.info("Active Profile = {}", activeProfile);
		}

		// for (int i = 0; i < 100; i++) {
		// 	CompletableFuture.runAsync(() -> {
		// 		try {
		// 			Thread.sleep(new Random().nextInt(10000));
		// 		} catch (InterruptedException e) {
		// 			e.printStackTrace();
		// 		}
		//
		// 		redisMessagePublisher.emit(
		// 			EventDto.builder()
		// 				.id(new Random().nextLong())
		// 				.message("helloworld")
		// 				.receiver(List.of(new Random().nextLong()))
		// 				.spaceId(new Random().nextLong())
		// 				.type(EventType.ROOM_EVENT)
		// 				.target(EventTarget.ROOM_AREA)
		// 				.build()
		// 		);
		// 	});
		// }
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper om = new ObjectMapper();
		om.registerModule(new JavaTimeModule());
		om.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return om;
	}

}
