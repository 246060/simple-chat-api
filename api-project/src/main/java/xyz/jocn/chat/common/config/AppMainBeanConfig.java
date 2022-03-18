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
import xyz.jocn.chat.file.repo.StorageLocalRepository;
import xyz.jocn.chat.file.repo.StorageRepository;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppMainBeanConfig {

	private final Environment environment;

	@PostConstruct
	public void init() {
		for (String defaultProfile : environment.getDefaultProfiles()) {
			log.info("Default Profile = {}", defaultProfile);
		}
		for (String activeProfile : environment.getActiveProfiles()) {
			log.info("Active Profile = {}", activeProfile);
		}
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper om = new ObjectMapper();
		om.registerModule(new JavaTimeModule());
		om.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
		om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return om;
	}

	@Bean
	public StorageRepository storageRepository() {
		return new StorageLocalRepository();
	}
}
