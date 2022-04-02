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
@ConfigurationProperties(prefix = "app.storage.aws-s3")
public class StorageAwsS3Properties implements StorageProperties {

	public StorageAwsS3Properties() {
		log.info("Created Bean : StorageAwsS3Properties");
	}
}
