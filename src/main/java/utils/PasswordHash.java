package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHash {
	// Hashes the password using BCrypt
	public static String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	// Verifies if the provided password matches the hashed password
	public static boolean verifyPassword(String password, String hashedPassword) {
		return BCrypt.checkpw(password, hashedPassword);
	}
}
