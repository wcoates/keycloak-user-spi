
package com.coates.keycloak.dataaccess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DataAccessFactoryTest {
    @Test
    void testCreateDataaccess() {
        assertEquals(true, DataAccessFactory.createDataAccess() != null);
    }
}
