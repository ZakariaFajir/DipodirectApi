package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import java.io.IOException;
import java.nio.charset.StandardCharsets; // Import for charset
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Properties;

public class TokenUtils {
    // Generate a JWT token for the specified subject (e.g., username)
    public static String generateToken(String username) throws IOException {
    	
    	String secretKey = "secretkey(())for!@@#dipodirect)__00";
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8); // Convert string to bytes
        SecretKey key = Keys.hmacShaKeyFor(keyBytes); // Generate secret key from byte array

        Instant now = Instant.now();
        Instant expiry = now.plus(1, ChronoUnit.HOURS); // Set token expiry time (e.g., 1 hour)

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(key)
                .compact();
    }

    public static String getUsernameFromToken(String token) {
        try {
        	String secretKey = "secretkey(())for!@@#dipodirect)__00";
            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8); // Convert string to bytes
            SecretKey key = Keys.hmacShaKeyFor(keyBytes); // Generate secret key from byte array

            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            return claims.getSubject(); // Extract the username from the token
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null if token is invalid or expired
        }
    }
}
