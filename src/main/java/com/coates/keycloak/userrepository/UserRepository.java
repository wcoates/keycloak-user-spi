
package com.coates.keycloak.userrepository;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.adapter.AbstractUserAdapter;

import com.coates.keycloak.dataaccess.DataAccess;

import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@RequiredArgsConstructor
public class UserRepository {
    private final DataAccess dataaccess;
    private final KeycloakSession session;
    private final ComponentModel model;

    public final UserLookup retrieveUser(final String username, final RealmModel realm) {
        log.info("Retrieving user from dataaccess");
        User userFromDataaccess = dataaccess.lookupUser(username);

        if (userFromDataaccess == null) {
            log.info("User not found in dataaccess");
            userFromDataaccess = new User("", username, "");
        }
        final UserLookup userLookup =
                new UserLookup(userFromDataaccess, adaptToUserModel(username, realm));

        return userLookup;
    }

    private final UserModel adaptToUserModel(final String username, final RealmModel realm) {
        return createAdapter(username, realm);
    }

    private final UserModel createAdapter(final String username, final RealmModel realm) {
        return new AbstractUserAdapter(session, realm, model) {
            @Override
            public String getUsername() {
                return username;
            }
        };
    }
}
