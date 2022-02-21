package com.example.keycloak.authenticator;

import java.util.List;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

// Mail OTP code authenticator factory
public class MailCodeAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

	// create singleton
	private static final MailCodeAuthenticator SINGLETON = new MailCodeAuthenticator();

	// provider ID
	public static final String PROVIDER_ID = "mail-authenticator";

	/* Requirement selector of the authenticator */
	private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
			AuthenticationExecutionModel.Requirement.REQUIRED,
			AuthenticationExecutionModel.Requirement.DISABLED
	};

	/* configurable items for the authenticator */
	private static final List<ProviderConfigProperty> configProperties;
	static {
		configProperties = ProviderConfigurationBuilder
				.create()
				// if configure items exist, set the item
				// .property()
				// .name()
				// .label()
				// .type(ProviderConfigProperty.STRING_TYPE)
				// .defaultValue()
				// .helpText()
				// .add()
				.build();
	}

	// return the authenticator instance.
	@Override
	public Authenticator create(KeycloakSession session) {
		return SINGLETON;
	}

	// return this provider id.
	@Override
	public String getId() {
		return PROVIDER_ID;
	}

	// return the configurable items.
	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
		return configProperties;
	}

	// return the information at tooltip.
	@Override
	public String getHelpText() {
		return "Mail Authenticate.";
	}

	// return this authenticator name.
	@Override
	public String getDisplayType() {
		return "Mail Authentication";
	}

	//  return the category of this authenticator.
	@Override
	public String getReferenceCategory() {
		return "sms-auth-code";
	}

	//  return if this authenticator is configurable in flow.
	@Override
	public boolean isConfigurable() {
		return true;
	}

	// return a requiement choices
	@Override
	public Requirement[] getRequirementChoices() {
		return REQUIREMENT_CHOICES;
	}

	// return SMSAuthenticator.setRequiredActions() is callable
	@Override
	public boolean isUserSetupAllowed() {
		return false;
	}

	// call when the authenticator is initiated.
	@Override
	public void init(Scope scope) {
	}

	// call when all provider is initiated.
	@Override
	public void postInit(KeycloakSessionFactory factory) {
	}

	// call when server shutdown 
	@Override
	public void close() {
	}
}
