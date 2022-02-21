# keycloak-mail-otp-spi
This is a service provider interface (SPI) of keycloak which can add one time pad (OTP) based two-factor authentication sending by email.
```mermaid
sequenceDiagram
  keycloak ->> user: show login page
  user ->> keycloak: enter uesrname and password
  keycloak ->> user's e-mail box: send authentication code
  user ->> keycloak: enter authentication code
```
## How to build and deploy
As described in [keycloak Doc](https://keycloak-documentation.openstandia.jp/master/ja_JP/server_development/index.html#_providers), this SPI can be build and deploy via below commands.
```
$ mvn package
$ cp ./custom-mail-auth/target/mail-authenticator.jar [KEYCLOAK_HOME]/provider/
$ cp ./custom-themes/target/mail-auth-theme.jar [KEYCLOAK_HOME]/provider/
```

## Component structure
```mermaid
classDiagram
  class MailCodeAuthenticator {
    +authenticate()
    +action()
  }

  class MailCodeAuthenticatorFactory {
    +create()
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
```