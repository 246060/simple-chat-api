package xyz.jocn.chat.common.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import xyz.jocn.chat.user.entity.UserEntity;

@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig implements AuditorAware<UserEntity> {

	@Override
	public Optional<UserEntity> getCurrentAuditor() {
		return Optional.empty();
	}
}
