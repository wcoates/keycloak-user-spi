
package com.coates.keycloak.dataaccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.coates.keycloak.dataaccess.impl.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@RequiredArgsConstructor
public class DataAccessManager implements DataAccess {
    private final Map<String, DataAccess> dataAccesses;

    @Override
    public final User lookupUser(final String username) {
        if (username.isBlank()) { return null; }

        final List<User> users = new ArrayList<>();

        dataAccesses.entrySet().stream().forEach(e -> {
            try {
                final User user = e.getValue().lookupUser(username);

                if (user != null) {
                    users.add(user);
                }
            } catch (final Exception ex) {
                log.error("Could not retrieve using using dataAccess " + e.getKey());
                ex.printStackTrace();
            }
        });

        if (!users.isEmpty()) {
            log.info("User to be returned: " + users.get(0));
        } else {
            log.info("Could not find a user in dataaccess.");
        }

        return users.isEmpty() ? null : users.get(0);
    }
}
