
package com.coates.keycloak.dataaccess;

import com.coates.keycloak.userrepository.User;

public interface DataAccess {
    User lookupUser(String username);
}
