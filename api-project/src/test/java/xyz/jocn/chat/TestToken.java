package xyz.jocn.chat;

import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class TestToken {

	private Long userId;
	private String issue;
	private Instant exp = Instant.now().plusSeconds(360000);
	private Set<String> scopes = new HashSet<>();
	private String secret = "jCIIGf7BKMMszsfSIXYS2v/CI1+aQ0fl4yZegXjVQLk=";

	public byte[] getKey() {
		return Base64.getDecoder().decode(secret);
	}

	public String generate(Long userId) throws JOSEException {
		return generate(userId, Collections.emptyList());
	}

	public String generate(Long userId, List<String> scopes) throws JOSEException {
		this.scopes.add("USER");
		for (String scope : scopes) {
			this.scopes.add(scope);
		}

		JWSSigner signer = new MACSigner(getKey());

		JWTClaimsSet.Builder claimSetBuilder = new JWTClaimsSet.Builder();
		claimSetBuilder.subject(String.valueOf(userId));
		claimSetBuilder.expirationTime(Date.from(exp));
		claimSetBuilder.claim("scope", scopes);
		JWTClaimsSet claimsSet = claimSetBuilder.build();

		SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
		signedJWT.sign(signer);
		return "Bearer " + signedJWT.serialize();
	}
}
