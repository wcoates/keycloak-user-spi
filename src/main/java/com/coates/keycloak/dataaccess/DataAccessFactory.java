
package com.coates.keycloak.dataaccess;

import com.coates.keycloak.dataaccess.impl.MysqlDataAccess;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DataAccessFactory {

    public static final DataAccess createDataAccess() {
        return new MysqlDataAccess();
    }
}
