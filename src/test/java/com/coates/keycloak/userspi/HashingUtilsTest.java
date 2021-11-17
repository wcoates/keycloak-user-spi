
package com.coates.keycloak.userspi;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HashingUtilsTest {
    private final String password = "password";
    private final String passwordHashValue =
            "$2a$12$6Ii8eBlaw1fQdBrIIHV8BOBhMowOJ2gNv6sGP4h2ZMjMSPJzcHsJ.";

    private final String securePassword = "X/Y#u>{xT5j*Su]j";
    private final String securePasswordHashValue =
            "$2a$12$C0Bfwylmzihaz72zbDJL..EuHZeUMDw4sFWcSXl7m1/.rXe2Ssqza";

    @Test
    void testCorrectPasswordMatch() {
        assertEquals(true, HashingUtils.isBcryptMatch(password, passwordHashValue));
    }

    @Test
    void testCorrectSecurePasswordMatch() {
        assertEquals(true, HashingUtils.isBcryptMatch(securePassword, securePasswordHashValue));
    }

    @Test
    void testEmptyPasswordMatch() {
        assertEquals(false, HashingUtils.isBcryptMatch("", passwordHashValue));
        assertEquals(false, HashingUtils.isBcryptMatch(" ", passwordHashValue));
        assertEquals(false, HashingUtils.isBcryptMatch(null, passwordHashValue));
    }

    @Test
    void testIncorrectPasswordMatch() {
        assertEquals(false, HashingUtils.isBcryptMatch("passwor", passwordHashValue));
        assertEquals(false, HashingUtils.isBcryptMatch("some string", passwordHashValue));
        assertEquals(false, HashingUtils.isBcryptMatch("*********", passwordHashValue));
    }
}
