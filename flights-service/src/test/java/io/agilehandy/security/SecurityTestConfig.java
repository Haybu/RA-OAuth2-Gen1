package io.agilehandy.security;

import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({MySecurityConfig.class, OAuth2AutoConfiguration.class})
public class SecurityTestConfig {

}