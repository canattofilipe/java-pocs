# Spring Security and Keycloak

## OAuth2
### Protect Access with an OAuth2 Access Token

To set up OAuth2 security with Spring Boot and Keycloak, follow these steps:

1. **Create a New Realm**: 
   - Navigate to the Keycloak admin console, click on "Realms", and then click "Add realm".
   - Name your realm and configure any necessary settings.

2. **Create a New Client**:
   - Within the created realm, navigate to "Clients" and click "Create".
   - Configure the client settings, such as Client ID and Client Secret. Note these values for later use.

3. **Create a New User**:
   - In the realm's "Users" section, click "Add user" to create a new user.
   - Set the user's credentials and any required attributes.

Once you've completed these steps, you'll have the necessary setup to authorize users against your Spring Boot application.

Update the `SecurityConfig` class to use your realm name. Replace `<my-realm-name>` with the name of your realm:

```java
@Bean
public JwtDecoder jwtDecoder() {
    return JwtDecoders.fromIssuerLocation("http://localhost:8180/realms/<my-realm-name>");
}
```

#### Testing
1. Get an Access Token from Keycloak:
```bash
curl --location 'http://localhost:8180/realms/<my-realm-name>/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'client_id=<client_id>' \
--data-urlencode 'client_secret=<client_secret>' \
--data-urlencode 'username=<username>' \
--data-urlencode 'password=<password>'
```
2. Access a Private Endpoint Protected by OAuth2:
```bash
curl --location 'http://localhost:8080/private/content' \
--header 'Authorization: Bearer <access_token>'
```

Ensure that the server responds with an HTTP status code 200, indicating successful access.