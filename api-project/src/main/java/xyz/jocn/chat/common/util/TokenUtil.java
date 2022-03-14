package xyz.jocn.chat.common.util;

import static xyz.jocn.chat.common.exception.TokenErrorCode.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;
import xyz.jocn.chat.auth.dto.JwtClaimsSetDto;
import xyz.jocn.chat.common.exception.TokenException;

@Slf4j
@Component
public class TokenUtil {

	@Value("${app.jwt-secret}")
	private String secret;

	@Value("${app.accessToken-expireInSec}")
	private int accessTokenExpireInSec;

	@Value("${app.refreshToken-expireInSec}")
	private int refreshTokenExpireInSec;

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuer;

	private final SecureRandom secureRandom = new SecureRandom(); // threadsafe
	private final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); // threadsafe

	private static final int AccessTokenByteLen = 32;
	private static final int RefreshTokenByteLen = 128;

	public String getIssuer() {
		return issuer;
	}

	public int getAccessTokenExpireInSec() {
		return accessTokenExpireInSec;
	}

	public int getRefreshTokenExpireInSec() {
		return refreshTokenExpireInSec;
	}

	public SecretKey getJwtKey() {
		byte[] decodedKey = Base64.getDecoder().decode(secret);
		return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
	}

	public String generateJwtKey(String password) {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey secretKey = keygen.generateKey();
			return Base64.getEncoder().encodeToString(secretKey.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
			throw new TokenException(WhenGenerateKey);
		}
	}

	public byte[] getKey() {
		return Base64.getDecoder().decode(secret);
	}

	public String generateRefreshToken() {
		byte[] randomBytes = new byte[RefreshTokenByteLen];
		secureRandom.nextBytes(randomBytes);
		return base64Encoder.encodeToString(randomBytes);
	}

	public String generateJwt(JwtClaimsSetDto claimsSetDto) {
		claimsSetDto.setExpirationTime(claimsSetDto.getIssueTime().plusSeconds(this.accessTokenExpireInSec));

		JWSSigner signer = null;
		try {
			signer = new MACSigner(getKey());
			JWTClaimsSet.Builder claimSetBuilder = new JWTClaimsSet.Builder();
			claimSetBuilder.subject(claimsSetDto.getSubject());
			claimSetBuilder.expirationTime(Date.from(claimsSetDto.getExpirationTime()));

			if (Objects.nonNull(claimsSetDto.getIssuer())) {
				claimSetBuilder.issuer(claimsSetDto.getIssuer());
			}
			if (Objects.nonNull(claimsSetDto.getIssueTime())) {
				claimSetBuilder.issueTime(Date.from(claimsSetDto.getIssueTime()));
			}
			if (Objects.nonNull(claimsSetDto.getNotBeforeTime())) {
				claimSetBuilder.notBeforeTime(Date.from(claimsSetDto.getNotBeforeTime()));
			}
			if (Objects.nonNull(claimsSetDto.getJwtID())) {
				claimSetBuilder.jwtID(claimsSetDto.getJwtID());
			}
			if (Objects.nonNull(claimsSetDto.getAudience())) {
				claimSetBuilder.audience(claimsSetDto.getAudience());
			}
			if (Objects.nonNull(claimsSetDto.getScope())) {
				claimSetBuilder.claim("scope", claimsSetDto.getScope());
			}

			JWTClaimsSet claimsSet = claimSetBuilder.build();
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
			signedJWT.sign(signer);

			return signedJWT.serialize();
		} catch (JOSEException e) {
			log.error(e.getMessage());
			throw new TokenException(WhenGenerateAccessToken);
		}
	}
}
