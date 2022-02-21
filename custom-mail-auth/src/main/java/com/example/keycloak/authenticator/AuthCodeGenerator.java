package com.example.keycloak.authenticator;

import java.security.SecureRandom;

class AuthCodeGenerator {

  static public String generate() {
		SecureRandom sr = new SecureRandom();
		String code = String.valueOf(sr.nextInt(1000000));
		return code;
  }
}