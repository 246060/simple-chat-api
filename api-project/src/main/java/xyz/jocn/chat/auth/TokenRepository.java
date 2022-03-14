package xyz.jocn.chat.auth;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
	Optional<TokenEntity> findByRefreshTokenAndRefreshExpireTimeAfter(String refreshToken, Instant now);
}
