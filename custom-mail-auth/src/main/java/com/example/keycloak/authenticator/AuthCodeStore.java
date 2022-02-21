package com.example.keycloak.authenticator;

import java.util.List;
import org.keycloak.models.UserCredentialManager;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.credential.CredentialModel;


class AuthCodeStore {
  UserCredentialManager userCredentialManager;
  RealmModel realm;
  UserModel user;

  public AuthCodeStore(UserCredentialManager userCredentialManager, RealmModel realm, UserModel user) {
    this.userCredentialManager = userCredentialManager;
    this.realm = realm;
    this.user = user;
  }

  public void save(String type, String value) {
		List<CredentialModel> creds = this.userCredentialManager.getStoredCredentialsByType(this.realm, this.user, type);

		if(creds.isEmpty()) {
			CredentialModel credential = new CredentialModel();
			credential.setType(type);
			credential.setValue(value);
			this.userCredentialManager.createCredential(this.realm, this.user, credential);
		} else {
			CredentialModel credential = creds.get(0);
			credential.setType(type);
			credential.setValue(value);
			this.userCredentialManager.updateCredential(this.realm, this.user, credential);
		}
  }

  public String getCode(String type) {
    CredentialModel cred = this.userCredentialManager.getStoredCredentialsByType(this.realm, this.user, type).get(0);
    if(cred != null) {
      return cred.getValue();
    } else {
      return null;
    }
  }
}