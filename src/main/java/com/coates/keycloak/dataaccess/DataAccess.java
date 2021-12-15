
package com.coates.keycloak.dataaccess;

import com.coates.keycloak.dataaccess.impl.User;

public interface DataAccess {
    User lookupUser(String username);
}
