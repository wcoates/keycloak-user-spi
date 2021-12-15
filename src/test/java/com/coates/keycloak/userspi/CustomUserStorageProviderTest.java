
package com.coates.keycloak.userspi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CustomUserStorageProviderTest {
    private static CustomUserStorageProviderFactory factory;
    private static CustomUserStorageProvider provider;

    @Test
    void testLookup() {
        assertEquals(true, provider.getUserByUsername(null, "").getUsername().equals(""));
        assertEquals(true, provider.getUserByUsername(null, "John").getUsername().equals("John"));
    }

    @Test
    void testUserModelLookup() {
        assertEquals(true, provider.getUserByUsername(null, "").getUsername().equals(""));
    }

    @BeforeAll
    public static void init() {
        factory = new CustomUserStorageProviderFactory();
        provider = factory.create(null, null);
    }
}
