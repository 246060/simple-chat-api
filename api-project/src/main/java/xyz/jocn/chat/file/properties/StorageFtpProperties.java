package xyz.jocn.chat.file.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.storage.ftp")
public class StorageFtpProperties implements StorageProperties {

	public StorageFtpProperties() {
		log.info("Created Bean : StorageFtpProperties");
	}
}
