package xyz.jocn.chat.file.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Primary
@Configuration
@ConfigurationProperties(prefix = "app.storage.local")
public class StorageLocalProperties implements StorageProperties {

	private String uploadRootPath;

	public StorageLocalProperties() {
		log.info("Created Bean : StorageLocalProperties");
	}
}
