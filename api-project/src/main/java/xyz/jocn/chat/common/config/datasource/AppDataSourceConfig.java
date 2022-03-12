package xyz.jocn.chat.common.config.datasource;

import static xyz.jocn.chat.common.config.datasource.AppDataSourceProperties.*;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile("prod")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties(AppDataSourceProperties.class)
public class AppDataSourceConfig {

	private final AppDataSourceProperties databaseProperty;
	private final JpaProperties jpaProperties;

	public AppDataSourceConfig(AppDataSourceProperties databaseProperty, JpaProperties jpaProperties) {
		this.databaseProperty = databaseProperty;
		this.jpaProperties = jpaProperties;
	}

	/**
	 * datasource 생성
	 */
	public DataSource createDataSource(String url) {
		log.info("databaseProperty {}", databaseProperty);
		log.info("jpaProperties {}", jpaProperties.getProperties());

		return DataSourceBuilder.create()
			.type(HikariDataSource.class)
			.url(url)
			//.driverClassName("com.mysql.cj.jdbc.Driver")
			.driverClassName(databaseProperty.getDriverClassName())
			//.driverClassName("org.mariadb.jdbc.Driver")
			.username(databaseProperty.getUsername())
			.password(databaseProperty.getPassword())
			.build();
	}

	/**
	 * 실제 쿼리가 실행될 때 Connection 을 가져오기
	 */
	@Bean
	public DataSource dataSource() {
		return new LazyConnectionDataSourceProxy(routingDataSource());
	}

	/**
	 * AppDataSourceProperties 를 통해 primary, replica Datasource 생성 후
	 * RoutingDataSource 에 등록
	 */
	@Bean
	public DataSource routingDataSource() {
		DataSource primary = createDataSource(databaseProperty.getUrl());

		Map<Object, Object> dataSourceMap = new LinkedHashMap<>();
		dataSourceMap.put(DATASOURCE_PRIMARY_NAME, primary);

		databaseProperty.getReplica()
			.forEach((key, value) -> dataSourceMap.put(value.getName(), createDataSource(value.getUrl())));

		log.info("Replica property : {}", databaseProperty.getReplica());

		RoutingDataSource routingDataSource = new RoutingDataSource();
		routingDataSource.setDefaultTargetDataSource(primary);
		routingDataSource.setTargetDataSources(dataSourceMap);

		return routingDataSource;
	}

	/**
	 * JPA 에서 사용할 EntityManagerFactory 설정
	 * hibernate 설정 직접 주입
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		EntityManagerFactoryBuilder entityManagerFactoryBuilder = createEntityManagerFactoryBuilder(jpaProperties);
		return entityManagerFactoryBuilder.dataSource(dataSource()).packages(PACKAGES_TO_SCAN).build();
	}

	private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
		AbstractJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		return new EntityManagerFactoryBuilder(vendorAdapter, jpaProperties.getProperties(), null);
	}

	/**
	 * JPA 에서 사용할 TransactionManager 설정
	 */
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(entityManagerFactory);
		return tm;
	}
}
