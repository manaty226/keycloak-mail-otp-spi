# keycloak-mail-otp-spi
This is a service provider interface (SPI) of keycloak which can add a one time pad (OTP) based two-factor authentication sending by email.
```mermaid
sequenceDiagram
  actor user
  participant keycloak
  keycloak ->> user: show login page
  user ->> keycloak: enter uesrname and password
  keycloak ->> keycloak: verify username and password
  keycloak -->> user: send authentication code by e-mail
  user ->> keycloak: enter authentication code
  keycloak ->> keycloak: verify authentication code
```
## How to build and deploy
As described in [keycloak Doc](https://keycloak-documentation.openstandia.jp/master/ja_JP/server_development/index.html#_providers), this SPI can be build and deploy via below commands.
```
$ mvn package
$ cp ./custom-mail-auth/target/mail-authenticator.jar [KEYCLOAK_HOME]/provider/
$ cp ./custom-themes/target/mail-auth-theme.jar [KEYCLOAK_HOME]/provider/
```
To send OTP via e-mail, SMTP configuration in your realm should be set up. 

## Component structure
```mermaid
classDiagram

  class AuthenticatorFactory {
    +create()  
  }
  
  class Authenticator {
    +authenticate()
    +action()  
  }

class MailCodeAuthenticator {
  -MAIL_ATTR
  -logger
}

  class MailCodeAuthenticatorFactory {
  }

  class MailClient {
    +sendMailCode()
  }

  class MailClientFactory {
    +create()
  }

  class AuthCodeGenerator {
    +static generate()
  }

  class AuthCodeStore {
    -userCredentialManager
    -realm
    -user
    +save()
    +getCode()
  }

  MailCodeAuthenticatorFactory --> MailCodeAuthenticator
  MailCodeAuthenticator --> MailClient
  MailCodeAuthenticator --> MailClientFactory
  MailCodeAuthenticator --> AuthCodeStore
  MailCodeAuthenticator --> AuthCodeGenerator
  MailClientFactory --> MailClient
  Authenticator <|-- MailCodeAuthenticator
  AuthenticatorFactory <|-- MailCodeAuthenticatorFactory
  MailClient --> EmailSenderProvider
  
```
