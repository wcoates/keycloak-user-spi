
package com.coates.keycloak.userrepository;

import org.keycloak.models.UserModel;

import lombok.Data;

@Data
public class UserLookup {
    private final User user;
    private final UserModel userModel;

    public final String getUsername() {
        return user.getUsername();
    }

    public final String getPassword() {
        return user.getPassword();
    }
}
