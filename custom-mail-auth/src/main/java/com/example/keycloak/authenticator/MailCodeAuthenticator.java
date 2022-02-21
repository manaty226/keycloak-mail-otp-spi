package com.example.keycloak.authenticator;

import java.util.Map;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;

public class MailCodeAuthenticator implements Authenticator {

	public static final String ATTR_EMAIL = "email";

	private static final Logger logger = Logger.getLogger(MailCodeAuthenticator.class);

	@Override
	public void authenticate(AuthenticationFlowContext context) {
		logger.debug("start authenticate()");

		Response challenge;
		UserModel user = context.getUser();
		RealmModel realm = context.getRealm();


		/* avoid to sending code when changing locale */
		String kc_locale = context.getUriInfo().getQueryParameters().getFirst("kc_locale");
		logger.info(kc_locale);
		if (kc_locale != null) {
			challenge = context.form().createForm("mail-validation.ftl");
			context.challenge(challenge);
			return;
		}

		AuthenticatorConfigModel config = context.getAuthenticatorConfig();
		String email = user.getFirstAttribute(ATTR_EMAIL);

		if (email != null) {
			KeycloakSession session = context.getSession();

			String code = AuthCodeGenerator.generate();
			AuthCodeStore authCodeStore = new AuthCodeStore(session.userCredentialManager(), realm, user);
			authCodeStore.save("mail-code", code);

			logger.info(code);

			MailClient mailClient = MailClientFactory.create(session);
			Map<String,String> smtpConfig = context.getRealm().getSmtpConfig();

			// send authentication code via email
			if (mailClient.sendMailCode(smtpConfig, user, code)) {
				challenge = context.form().createForm("mail-validation.ftl");
			} else {
				challenge = context.form().addError(new FormMessage("sendMailCodeErrorMessage"))
						.createForm("mail-validation-error.ftl");
			}
		} else {
			// error not to set email attribute
			challenge = context.form().addError(new FormMessage("missingEmailMessage"))
					.createForm("mail-validation-error.ftl");
		}
		context.challenge(challenge);
		logger.debug("end authenticate()");
	}

	@Override
	public void action(AuthenticationFlowContext context) {
		logger.debug("start action()");

		UserModel user = context.getUser();
		RealmModel realm = context.getRealm();
		KeycloakSession session = context.getSession();

		AuthCodeStore authCodeStore = new AuthCodeStore(session.userCredentialManager(), realm, user);
		String expectedCode = authCodeStore.getCode("mail-code");
		String inputCode = context.getHttpRequest().getDecodedFormParameters().getFirst("mailCode");

		// check if the entered code is valid.
		if (expectedCode.equals(inputCode)) {
			context.success();
		} else {
			Response challenge = context.form()
					.setAttribute("username", context.getAuthenticationSession().getAuthenticatedUser().getUsername())
					.addError(new FormMessage("invalidMailCodeMessage")).createForm("mail-validation-error.ftl");
			context.challenge(challenge);
		}
		logger.debug("end action()");
	}

	@Override
	public boolean requiresUser() {
		return true;
	}

	@Override
	public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
		return true;
	}

	@Override
	public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
	}

	@Override
	public void close() {
	}
}
