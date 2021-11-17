
package com.coates.keycloak.userspi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CustomUserStorageProviderFactoryTest {
    @Test
    void testUserStorageProviderFactoryCreate() {
        final CustomUserStorageProviderFactory factory = new CustomUserStorageProviderFactory();
        assertEquals(true, factory.create(null, null) != null);
    }
}
