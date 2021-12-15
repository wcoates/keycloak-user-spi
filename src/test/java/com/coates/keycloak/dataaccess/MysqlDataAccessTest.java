
package com.coates.keycloak.dataaccess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.coates.keycloak.dataaccess.impl.MysqlDataAccess;

public class MysqlDataAccessTest {
    private final DataAccess mysqlDataAccess = DataAccessFactory.createDataAccessByName("mysql");

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
            "SELECT MEMBER_ID, EMAIL, PASSWORD, DELETED_AT FROM users WHERE email=\"wcoates\" LIMIT 1";
    private static final String usernameToLookup = "wcoates";
    private static final int RECORD_LIMIT = 1;

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
        assertEquals(true,
                MysqlDataAccess
                        .constructQuery(columnNames, TABLE_NAME, usernameToLookup, RECORD_LIMIT)
                        .equals(constructedQuery));
    }
}
