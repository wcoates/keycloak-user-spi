
package com.coates.keycloak.dataaccess;

import com.coates.keycloak.userrepository.User;

public interface Dataaccess {
	User lookupUser(String username);
}
