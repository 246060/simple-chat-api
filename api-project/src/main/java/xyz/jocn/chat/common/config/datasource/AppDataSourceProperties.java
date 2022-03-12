package xyz.jocn.chat.common.config.datasource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class AppDataSourceProperties {

	public static final String DATASOURCE_PRIMARY_NAME = "read-write";
	public static final String DATASOURCE_REPLICA_NAME = "read-only";
	public static final String PACKAGES_TO_SCAN = "xyz.jocn.chat";

	private String url;
	private String username;
	private String password;
	private String driverClassName;

	private final Map<String, Replica> replica = new HashMap<>();

	@Data
	public static class Replica {
		private String name;
		private String url;
	}
}
