
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
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.ReadOnlyException;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

import com.coates.keycloak.userrepository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomUserStorageProvider implements UserStorageProvider, UserLookupProvider,
		CredentialInputValidator, CredentialInputUpdater
{
	private final UserRepository userRepository;
	protected KeycloakSession session;
	protected ComponentModel model;

	@Override
	public void close() {
		//
	}

	@Override
	public boolean supportsCredentialType(final String credentialType) {
		return credentialType.equals(CredentialModel.PASSWORD);
	}

	@Override
	public boolean isConfiguredFor(final RealmModel realm, final UserModel userModel,
			final String credentialType)
	{
		final String password = lookupPassword(userModel, realm);

		return credentialType.equals(CredentialModel.PASSWORD) && password != null;
	}

	@Override
	public boolean isValid(final RealmModel realm, final UserModel userModel,
			final CredentialInput credentialInput)
	{
		if (
			!supportsCredentialType(credentialInput.getType())
					|| !(credentialInput instanceof UserCredentialModel)
		) return false;

		final UserCredentialModel cred = (UserCredentialModel) credentialInput;
		final String password = lookupPassword(userModel, realm);

		if (password == null) return false;

		return EncryptionUtils.isBcryptMatch(cred.getValue(), password);
	}

	@Override
	public UserModel getUserById(final String id, final RealmModel realm) {
		final StorageId storageId = new StorageId(id);
		final String username = storageId.getExternalId();

		return getUserByUsername(username, realm);
	}

	@Override
	public UserModel getUserByUsername(final String username, final RealmModel realm) {
		final UserModel adapter = userRepository.retrieveUser(username, realm).getUserModel();

		if (adapter == null) {
			// User was not found!
		}

		return adapter;
	}

	@Override
	public UserModel getUserByEmail(final String email, final RealmModel realm) {
		// TODO Auto-generated method stub
		return null;
	}

	// For making passwords read-only
	@Override
	public boolean updateCredential(final RealmModel realm, final UserModel userModel,
			final CredentialInput input)
	{
		if (
			input.getType().equals(CredentialModel.PASSWORD)
		) throw new ReadOnlyException("user is read only for this update");

		return false;
	}

	@Override
	public void disableCredentialType(final RealmModel realm, final UserModel userModel,
			String credentialType)
	{

	}

	@Override
	public Set<String> getDisableableCredentialTypes(final RealmModel realm,
			final UserModel userModel)
	{
		return Collections.emptySet();
	}

	private final String lookupPassword(final UserModel userModel, final RealmModel realm) {
		return userRepository.retrieveUser(userModel.getUsername(), realm).getPassword();
	}
}
