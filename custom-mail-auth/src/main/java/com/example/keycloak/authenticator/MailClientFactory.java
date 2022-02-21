package com.example.keycloak.authenticator;

import org.keycloak.models.KeycloakSession;
import org.keycloak.email.DefaultEmailSenderProvider;
import org.keycloak.email.EmailSenderProvider;

class MailClientFactory {

  static public MailClient create(KeycloakSession session) {
    EmailSenderProvider emailSender = new DefaultEmailSenderProvider(session);
    return new MailClient(emailSender);
  }
}