
package com.coates.keycloak.userspi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashingUtils {

    public static final boolean isBcryptMatch(final String value, final String hashValue) {
        if (value == null || hashValue == null || value.isEmpty() || hashValue.isEmpty()) {
            return false;
        }
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.matches(value, hashValue);
    }
}
