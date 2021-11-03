
package com.coates.keycloak.dataaccess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DataaccessFactoryTest {
	@Test
	void testCreateDataaccess() {
		assertEquals(true, DataaccessFactory.createDataaccess() != null);
	}
}
