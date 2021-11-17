
package com.coates.keycloak.userrepository;

import lombok.Data;

@Data
public class User {
    private final String user_id;
    private final String username;
    private final String password;
}
