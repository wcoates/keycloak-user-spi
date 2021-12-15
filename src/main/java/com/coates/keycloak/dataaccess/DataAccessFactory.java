
package com.coates.keycloak.dataaccess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coates.keycloak.dataaccess.impl.DynamoDataAccess;
import com.coates.keycloak.dataaccess.impl.MysqlDataAccess;

import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@RequiredArgsConstructor
public class DataAccessFactory {
    private static final List<String> DA_CONFIG = Arrays.asList("mysql", "dynamo");

    public static final Map<String, DataAccess> createDataAccessImpls() {
        final Map<String, DataAccess> dataAccessList = new HashMap<>();

        DA_CONFIG.forEach(e -> {
            try {
                dataAccessList.put(e, createDataAccessByName(e));
            } catch (final Exception ex) {
                log.error(ex);
            }
        });

        if (dataAccessList.isEmpty()) {
            log.error("Error during dataAccess configuration or configuration is missing.");
        }

        return dataAccessList;
    }

    public static final DataAccess createDataAccessByName(final String dataAccessName) {
        if (dataAccessName.equals("mysql")) { return new MysqlDataAccess(); }

        if (dataAccessName.equals("dynamo")) { return new DynamoDataAccess(); }

        log.error("No dataAccess defined for dataAccessName: " + dataAccessName);

        return null;
    }
}
