package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class TokenUtils {
    private static final String SECRET_KEY = System.getenv("SECRET_KEY");
	// Generate a JWT token for the specified subject (e.g., email)
	public static String generateToken(String email) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

			Instant now = Instant.now();
			Instant expiry = now.plus(Duration.ofHours(1)); // Set token expiry time (e.g., 1 hour)

			return Jwts.builder().setSubject(email).setIssuedAt(Date.from(now)).setExpiration(Date.from(expiry))
					.signWith(key).compact();
		} catch (Exception e) {
			e.printStackTrace();
			return null; // Return null if token generation fails
		}
	}

	public static String getEmailFromToken(String token) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
			Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return claimsJws.getBody().getSubject(); // Extract the Email from the token
		} catch (Exception e) {
			e.printStackTrace();
			return null; // Return null if token is invalid or expired
		}
	}
}
