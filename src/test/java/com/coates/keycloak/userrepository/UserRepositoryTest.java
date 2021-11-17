
package com.coates.keycloak.userrepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import com.coates.keycloak.dataaccess.DataAccessFactory;

public class UserRepositoryTest {
    private static UserRepository userRepository;

    @RepeatedTest(10)
    void testUserLookup() {
        final String generatedString = RandomStringUtils.randomAlphabetic(10);
        final UserLookup lookup = userRepository.retrieveUser(generatedString, null);

        assertEquals(true, lookup != null);
        assertEquals(true, lookup.getUsername() != null);
        assertEquals(true, lookup.getPassword() != null);

        assertEquals(true, lookup.getUser() != null);
        assertEquals(true, lookup.getUser().getUsername() != null);
        assertEquals(true, lookup.getUser().getUsername().equals(generatedString));

        assertEquals(true, lookup.getUserModel() != null);
        assertEquals(true, lookup.getUserModel().getUsername() != null);
        assertEquals(true, lookup.getUserModel().getUsername().equals(generatedString));
    }

    @BeforeAll
    public static void init() {
        userRepository = new UserRepository(DataAccessFactory.createDataAccess(), null, null);
    }
}
