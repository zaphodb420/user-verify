# user-verify
Password maangement and login management Rest API

## Run and install instructions
mvn install - installs the application, compiles the code and runs test suite
mvn test - run test suite only

## Used stack
- Framework: Spring boot
- Language: Kotlin
- Deployment: Maven
- Testing: JUnit
- Coverage: Jacoco

## High level description
The application is build in three layers:
- **Controller layer**: Rest API calls defintions
- **Service layer**: Bussiness logic implementation
- **DAO**: Database access

## Supported functionality

**User management**
- Add new user
- Change password
- Delete user (with password verification)

**Login management**
- Login
- Logout

Logout procedure is verified by a token, returned when a successful login is done. This token is actually the User's ID, but that is only for simplicity puposes (Random token would've been safer to use)
