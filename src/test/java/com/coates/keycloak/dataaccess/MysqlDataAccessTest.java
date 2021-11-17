
package com.coates.keycloak.dataaccess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.coates.keycloak.dataaccess.impl.MysqlDataAccess;

public class MysqlDataAccessTest {
    private final DataAccess mysqlDataAccess = DataAccessFactory.createDataAccess();

    private final String hostname = "127.0.0.1";
    private final Integer port = 3306;
    private final String databaseName = "users";
    private final String user = "root";
    private final String password = "securerootpassword";
    private final String constructedConnectionUrl =
            "jdbc:mysql://127.0.0.1:3306/users?user=root&password=securerootpassword";
    private static final String TABLE_NAME = "users";
    private static final String[] columnNames = {"MEMBER_ID", "EMAIL", "PASSWORD", "DELETED_AT"};
    private static final String constructedQuery =
            "select MEMBER_ID, EMAIL, PASSWORD, DELETED_AT from users";

    @Test
    void testConstructConnection() {
        assertEquals(true, constructedConnectionUrl.equals(
                MysqlDataAccess.constructConnection(hostname, port, databaseName, user, password)));
    }

    @Test
    void testNoMatchLookup() {
        assertEquals(true, mysqlDataAccess.lookupUser("") == null);
    }

    @Test
    void testConstructQuery() {
        System.out.println(MysqlDataAccess.constructQuery(columnNames, TABLE_NAME));
        assertEquals(true,
                MysqlDataAccess.constructQuery(columnNames, TABLE_NAME).equals(constructedQuery));
    }
}
