
package com.coates.keycloak.dataaccess.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.coates.keycloak.dataaccess.Dataaccess;
import com.coates.keycloak.userrepository.User;

public class MysqlDataaccess implements Dataaccess {
	private static final String HOSTNAME = "localhost";
	private static final String PORT = ":3306";
	private static final String DATABASE_NAME = "mysql";
	private static final String USER = "root";
	private static final String PASSWORD = "password";

	private final Connection connection = createConnection();

	@Override
	public final User lookupUser(final String username) {
		return retrieveUser(username);
	}

	private final Connection createConnection() {
		Connection conn = null;

		try {
			System.out.println("Creating connection");

			conn = DriverManager.getConnection(
					constructConnection(HOSTNAME, PORT, DATABASE_NAME, USER, PASSWORD));
			System.out.println("Connection constructed");

		} catch (SQLException ex) {
			System.out.println("Connection exception");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception ex) {
			System.out.println("Error while creating connection");
			System.out.println(ex.getMessage());
		}

		return conn;
	}

	private final User retrieveUser(final String username) {
		final String query = "select MEMBER_ID, EMAIL, PASSWORD, DELETED_AT from USERS";

		try {
			if (connection != null) {
				final Statement stmt = connection.createStatement();
				System.out.println("statement constructed");

				if (stmt != null) {
					System.out.println("executing query");

					final ResultSet rs = stmt.executeQuery(query);
					return constructUserFromResultSet(rs);
				}
			}
		} catch (SQLException e) {
			System.out.println("query exception");
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		} catch (Exception e) {
			System.out.println("General query exception");
			System.out.println(e);
			System.out.println(e.getMessage());
		}

		System.out.println("Finishing retrieve user");

		return null;
	}


	private final User constructUserFromResultSet(final ResultSet rs) throws Exception {
		if (rs != null) {
			while (rs.next()) {
				System.out.println("ResultSet:" + rs.getString("MEMBER_ID") + " "
						+ rs.getString("EMAIL") + " " + rs.getString("PASSWORD"));
				if (rs.getString("DELETED_AT") != null) {
					return new User(rs.getString("MEMBER_ID"), rs.getString("EMAIL"),
							rs.getString("PASSWORD"));
				}
			}
		}

		return null;
	}

	public static final String constructConnection(String hostname, String port, String tableName,
			String user, String password)
	{
		return "jdbc:mysql://" + hostname + port + "/" + tableName + "?" + "user=" + user
				+ "&password=" + password;
	}
}
