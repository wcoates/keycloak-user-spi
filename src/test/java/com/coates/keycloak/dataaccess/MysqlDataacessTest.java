
package com.coates.keycloak.dataaccess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.coates.keycloak.dataaccess.impl.MysqlDataaccess;

public class MysqlDataacessTest {
	private final Dataaccess mysqlDataaccess = DataaccessFactory.createDataaccess();

	private final String hostname = "127.0.0.1";
	private final String port = ":3306";
	private final String databaseName = "users";
	private final String user = "root";
	private final String password = "securerootpassword";
	private final String constructedConnectionUrl =
			"jdbc:mysql://127.0.0.1:3306/users?user=root&password=securerootpassword";

	@Test
	void testConstructConnection() {
		assertEquals(true, constructedConnectionUrl.equals(
				MysqlDataaccess.constructConnection(hostname, port, databaseName, user, password)));
	}

	@Test
	void testNoMatchLookup() {
		assertEquals(true, mysqlDataaccess.lookupUser("") == null);
	}
}
