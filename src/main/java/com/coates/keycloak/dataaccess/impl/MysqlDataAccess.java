
package com.coates.keycloak.dataaccess.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.coates.keycloak.dataaccess.DataAccess;
import com.coates.keycloak.userrepository.User;

import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class MysqlDataAccess implements DataAccess {
    private static final String HOSTNAME = "keycloak_mysql";
    private static final Integer PORT = 3306;
    private static final String DATABASE_NAME = "members";
    private static final String TABLE_NAME = "users";
    private static final String[] COLUMN_NAMES = {"MEMBER_ID", "EMAIL", "PASSWORD", "DELETED_AT"};
    private static final String USER = "root";
    private static final String PASSWORD = "password";
    private static final int RECORD_LIMIT = 1;

    private final Connection connection = createConnection();

    @Override
    public final User lookupUser(final String username) {
        return retrieveUser(username);
    }

    private final Connection createConnection() {
        Connection conn = null;

        try {
            final String connection =
                    constructConnection(HOSTNAME, PORT, DATABASE_NAME, USER, PASSWORD);
            conn = DriverManager.getConnection(connection);
        } catch (final SQLException e) {
            logSqlException(e);
        } catch (final Exception e) {
            log.error("Error while creating connection\n" + e.getMessage());
        }

        return conn;
    }

    private final User retrieveUser(final String username) {
        final String query = constructQuery(COLUMN_NAMES, TABLE_NAME, RECORD_LIMIT);

        try {
            if (connection != null) {
                final Statement stmt = connection.createStatement();
                log.info("Statement constructed");

                if (stmt != null) {
                    log.info("Executing query");
                    return constructUserFromResultSet(stmt.executeQuery(query));
                }
            }
        } catch (final SQLException e) {
            logSqlException(e);
        } catch (final Exception e) {
            log.error("General query exception\n" + e.getMessage());
        }

        return null;
    }

    private final User constructUserFromResultSet(final ResultSet rs) throws Exception {
        if (rs != null) {
            while (rs.next()) {
                if (!isDeletedUser(rs)) {
                    return new User(rs.getString("MEMBER_ID"), rs.getString("EMAIL"),
                            rs.getString("PASSWORD"));
                }
            }
        }

        return null;
    }

    public static final boolean isDeletedUser(final ResultSet rs) {
        try {
            return rs.getString("DELETED_AT") != null;
        } catch (SQLException e) {
            logSqlException(e);
        }

        return false;
    }

    public static final String constructConnection(final String hostname, final Integer port,
            final String tableName, final String user, final String password)
    {
        return "jdbc:mysql://" + hostname + ":" + port + "/" + tableName + "?" + "user=" + user
                + "&password=" + password;
    }

    public static final String constructQuery(final String[] columnNames, final String tableName,
            final int recordLimit)
    {
        return "select " + String.join(", ", columnNames) + " from " + TABLE_NAME + " LIMIT "
                + recordLimit;
    }

    private static final void logSqlException(final SQLException e) {
        log.error("SQLException: " + e.getMessage());
        log.error("SQLState: " + e.getSQLState());
        log.error("VendorError: " + e.getErrorCode());
    }
}
