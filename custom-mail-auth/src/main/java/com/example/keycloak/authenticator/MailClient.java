package com.example.keycloak.authenticator;

import org.jboss.logging.Logger;

import java.util.Map;


import org.keycloak.email.EmailSenderProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.models.RealmModel;


public class MailClient {

	private static final Logger logger = Logger.getLogger(MailClient.class);
	private EmailSenderProvider emailSender;

	public MailClient(EmailSenderProvider emailSender) {
		this.emailSender = emailSender;
	}

	public boolean sendMailCode(Map<String,String> smtpConfig, UserModel user, String code) {
		logger.debug("start sendMail()");
		try {
			this.emailSender.send(smtpConfig, user, "login authentication code", code, "<p>Login code: "+code+"</p>");
		} catch(Exception e) {
			return false;
		}
		return true;
	}
}
