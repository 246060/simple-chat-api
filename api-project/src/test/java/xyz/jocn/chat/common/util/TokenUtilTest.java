package xyz.jocn.chat.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

class TokenUtilTest {

	@Test
	void e() throws NoSuchAlgorithmException, JOSEException, ParseException {

		KeyGenerator keygen = KeyGenerator.getInstance("HmacSHA256");
		SecretKey originalKey = keygen.generateKey();
		String key = Base64.getEncoder().encodeToString(originalKey.getEncoded());

		JWSSigner signer = new MACSigner(originalKey);
		JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload("Hello, world!"));
		jwsObject.sign(signer);
		String s = jwsObject.serialize();

		jwsObject = JWSObject.parse(s);

		byte[] decodedKey = Base64.getDecoder().decode(key);
		SecretKey key2 = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
		JWSVerifier verifier = new MACVerifier(key2);

		assertTrue(jwsObject.verify(verifier));
	}

	@Test
	void d() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(256);
		SecretKey originalKey = keyGenerator.generateKey();
		byte[] bytes = originalKey.getEncoded();

		String key = Base64.getEncoder().encodeToString(bytes);
		System.out.println("key = " + key);

		byte[] decodedKey = Base64.getDecoder().decode(key);
		SecretKeySpec aes = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
	}

	/*
	 * access token 에 필요한 키이다.
	 * 여기서 생성한 뒤 환경 변수에 추가해주면 된다.
	 * */
	@DisplayName("패스워드 암호화")
	@Test
	void generateJwtKey() {
		String password = "password";

		String key = new TokenUtil().generateJwtKey(password);
		System.out.println("key = " + key);

		Assertions.assertThat(key).isNotEmpty();
	}

	@DisplayName("nimbusds-jwt 사용법 1")
	@Test
	void t1() throws JOSEException, ParseException {

		// Generate random 256-bit (32-byte) shared secret
		SecureRandom random = new SecureRandom();
		byte[] sharedSecret = new byte[32];
		random.nextBytes(sharedSecret);

		// Create HMAC signer
		JWSSigner signer = new MACSigner(sharedSecret);

		// Prepare JWS object with "Hello, world!" payload
		JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload("Hello, world!"));

		// Apply the HMAC
		jwsObject.sign(signer);

		// To serialize to compact form, produces something like
		// eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
		String s = jwsObject.serialize();

		// To parse the JWS and verify it, e.g. on client-side
		jwsObject = JWSObject.parse(s);

		JWSVerifier verifier = new MACVerifier(sharedSecret);

		assertTrue(jwsObject.verify(verifier));

		assertEquals("Hello, world!", jwsObject.getPayload().toString());
	}

	@DisplayName("nimbusds-jwt 사용법 2")
	@Test
	void t2() throws JOSEException, ParseException {
		// Generate random 256-bit (32-byte) shared secret
		SecureRandom random = new SecureRandom();
		byte[] sharedSecret = new byte[32];
		random.nextBytes(sharedSecret);

		// Create HMAC signer
		JWSSigner signer = new MACSigner(sharedSecret);

		// Prepare JWT with claims set
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
			.subject("alice")
			.issuer("https://c2id.com")
			.expirationTime(new Date(new Date().getTime() + 60 * 1000))
			.build();

		SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

		// Apply the HMAC protection
		signedJWT.sign(signer);

		// Serialize to compact form, produces something like
		// eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
		String s = signedJWT.serialize();

		// On the consumer side, parse the JWS and verify its HMAC
		signedJWT = SignedJWT.parse(s);

		JWSVerifier verifier = new MACVerifier(sharedSecret);

		assertTrue(signedJWT.verify(verifier));

		// Retrieve / verify the JWT claims according to the app requirements
		assertEquals("alice", signedJWT.getJWTClaimsSet().getSubject());
		assertEquals("https://c2id.com", signedJWT.getJWTClaimsSet().getIssuer());
		assertTrue(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()));
	}
}