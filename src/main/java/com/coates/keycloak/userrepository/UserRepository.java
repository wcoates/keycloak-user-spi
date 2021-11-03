
package com.coates.keycloak.userrepository;

import java.util.HashMap;
import java.util.Map;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.adapter.AbstractUserAdapter;

import com.coates.keycloak.dataaccess.Dataaccess;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepository {
	private final Dataaccess dataaccess;
	private final Map<String, UserLookup> loadedUsers = new HashMap<>();
	private final KeycloakSession session;
	private final ComponentModel model;

	public final UserLookup retrieveUser(final String username, final RealmModel realm) {
		UserLookup userLookup = retrieveUserFromCache(username);

		if (userLookup != null) { return userLookup; }

		User userFromDataaccess = dataaccess.lookupUser(username);

		if (userFromDataaccess == null) {
			userFromDataaccess = new User("", username, "");
		} else {
			loadedUsers.put(username, userLookup);
		}
		userLookup = new UserLookup(userFromDataaccess, adaptToUserModel(username, realm));

		return userLookup;
	}

	private final UserLookup retrieveUserFromCache(final String username) {
		final UserLookup userLookup = loadedUsers.get(username);

		if (userLookup != null) { return loadedUsers.get(username); }

		return null;
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
