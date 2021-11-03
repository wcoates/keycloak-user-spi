
package com.coates.keycloak.userspi;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EncryptionUtilsTest {
	private final String password = "password";
	private final String passwordHashValue =
			"$2a$12$6Ii8eBlaw1fQdBrIIHV8BOBhMowOJ2gNv6sGP4h2ZMjMSPJzcHsJ.";

	private final String securePassword = "X/Y#u>{xT5j*Su]j";
	private final String securePasswordHashValue =
			"$2a$12$C0Bfwylmzihaz72zbDJL..EuHZeUMDw4sFWcSXl7m1/.rXe2Ssqza";

	@Test
	void testCorrectPasswordMatch() {
		assertEquals(true, EncryptionUtils.isBcryptMatch(password, passwordHashValue));
	}

	@Test
	void testCorrectSecurePasswordMatch() {
		assertEquals(true, EncryptionUtils.isBcryptMatch(securePassword, securePasswordHashValue));
	}

	@Test
	void testEmptyPasswordMatch() {
		assertEquals(false, EncryptionUtils.isBcryptMatch("", passwordHashValue));
		assertEquals(false, EncryptionUtils.isBcryptMatch(" ", passwordHashValue));
		assertEquals(false, EncryptionUtils.isBcryptMatch(null, passwordHashValue));
	}

	@Test
	void testIncorrectPasswordMatch() {
		assertEquals(false, EncryptionUtils.isBcryptMatch("passwor", passwordHashValue));
		assertEquals(false, EncryptionUtils.isBcryptMatch("some string", passwordHashValue));
		assertEquals(false, EncryptionUtils.isBcryptMatch("*********", passwordHashValue));
	}
}
