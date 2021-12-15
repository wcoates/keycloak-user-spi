
package com.coates.keycloak.userspi;

import java.util.Properties;

import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

import com.coates.keycloak.dataaccess.DataAccessFactory;
import com.coates.keycloak.dataaccess.DataAccessManager;
import com.coates.keycloak.userrepository.UserRepository;

public class CustomUserStorageProviderFactory
        implements UserStorageProviderFactory<CustomUserStorageProvider>
{
    public static final String PROVIDER_NAME = "custom-user-storage";
    protected Properties properties = new Properties();

    @Override
    public void init(Config.Scope config) {
        //
    }

    @Override
    public String getId() {
        return PROVIDER_NAME;
    }

    @Override
    public CustomUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        final UserRepository userRepository = new UserRepository(
                new DataAccessManager(DataAccessFactory.createDataAccessImpls()), session, model);

        return new CustomUserStorageProvider(userRepository, session, model);
    }
}
