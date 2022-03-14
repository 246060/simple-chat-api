package xyz.jocn.chat.auth.dto;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
public class JwtClaimsSetDto {

	private String subject;
	private Instant expirationTime;
	private Instant issueTime;
	private String issuer;
	private Instant notBeforeTime;
	private String jwtID;
	private List<String> audience;
	private Set<String> scope;

	@Builder
	public JwtClaimsSetDto(String subject, Instant expirationTime, String issuer,
		Instant notBeforeTime, String jwtID, List<String> audience, Set<String> scope) {
		this.subject = subject;
		this.expirationTime = expirationTime;
		this.issueTime = Instant.now();
		this.issuer = issuer;
		this.notBeforeTime = notBeforeTime;
		this.jwtID = jwtID;
		this.audience = audience;
		this.scope = scope;
	}
}
