
package com.coates.keycloak.userspi;

import java.util.Collections;
import java.util.Set;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.ReadOnlyException;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

import com.coates.keycloak.userrepository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@AllArgsConstructor
public class CustomUserStorageProvider implements UserStorageProvider, UserLookupProvider,
        CredentialInputValidator, CredentialInputUpdater
{
    private final UserRepository userRepository;
    protected final KeycloakSession session;
    protected final ComponentModel model;

    @Override
    public final void close() {
        //
    }

    @Override
    public final boolean supportsCredentialType(final String credentialType) {
        return credentialType.equals(CredentialModel.PASSWORD);
    }

    @Override
    public final boolean isConfiguredFor(final RealmModel realm, final UserModel userModel,
            final String credentialType)
    {
        final String password = lookupPassword(userModel, realm);

        return credentialType.equals(CredentialModel.PASSWORD) && password != null;
    }

    @Override
    public final boolean isValid(final RealmModel realm, final UserModel userModel,
            final CredentialInput credentialInput)
    {
        final String storedPassword = lookupPassword(userModel, realm);
        if (storedPassword == null) { return false; }

        final String givenPassword = credentialInput.getChallengeResponse();
        if (!supportsCredentialType(credentialInput.getType())) { return false; }

        return HashingUtils.isBcryptMatch(givenPassword, storedPassword);
    }

    @Override
    public final UserModel getUserById(final String id, final RealmModel realm) {
        final StorageId storageId = new StorageId(id);
        final String username = storageId.getExternalId();

        return getUserByUsername(realm, username);
    }

    @Override
    public final UserModel getUserByUsername(final String username, final RealmModel realm) {
        final UserModel adapter = userRepository.retrieveUser(username, realm).getUserModel();

        if (adapter == null) {
            log.info("Could not find user with username: {" + username + "}.");
        }

        return adapter;
    }

    @Override
    public final UserModel getUserByEmail(final String email, final RealmModel realm) {
        return null;
    }

    @Override
    public final boolean updateCredential(final RealmModel realm, final UserModel userModel,
            final CredentialInput input)
    {
        if (input.getType().equals(CredentialModel.PASSWORD)) {
            throw new ReadOnlyException("user is read only for this update");
        }

        return false;
    }

    @Override
    public final void disableCredentialType(final RealmModel realm, final UserModel userModel,
            String credentialType)
    {
        //
    }

    @Override
    public final Set<String> getDisableableCredentialTypes(final RealmModel realm,
            final UserModel userModel)
    {
        return Collections.emptySet();
    }

    private final String lookupPassword(final UserModel userModel, final RealmModel realm) {
        return userRepository.retrieveUser(userModel.getUsername(), realm).getPassword();
    }
}
