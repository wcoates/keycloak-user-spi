
package com.coates.keycloak.dataaccess.impl;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.coates.keycloak.dataaccess.DataAccess;

import lombok.Data;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class DynamoDataAccess implements DataAccess {

    private static final AmazonDynamoDB client =
            AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

    @Override
    public final User lookupUser(final String username) {
        final DynamoDBMapper mapper = new DynamoDBMapper(client);
        final UserQuery userQuery = retrieveUser(mapper, username, UserQuery.class);

        return convertToUser(userQuery);
    }

    private final <T> T retrieveUser(final DynamoDBMapper mapper, final Object hashKey,
            final Class<?> className)
    {
        try {
            @SuppressWarnings("unchecked")
            final T objectOfT = (T) mapper.load(className, hashKey);
            return objectOfT;
        } catch (final Exception e) {
            log.error("Could not retrieve object of type " + className);
            log.error(e);
        }

        return null;
    }

    // Class must be public/static
    @DynamoDBTable(tableName = "Users")
    @Data
    public static class UserQuery {
        @DynamoDBHashKey(attributeName = "username")
        private String username;

        @DynamoDBAttribute(attributeName = "password")
        private String password;
    }

    public static final User convertToUser(final UserQuery userQuery) {
        return userQuery == null ? null
                : new User(userQuery.getUsername(), userQuery.getPassword());
    }
}
