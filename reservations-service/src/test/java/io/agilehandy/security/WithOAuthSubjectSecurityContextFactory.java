package io.agilehandy.security;

import com.google.common.collect.ImmutableMap;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Map;

public class WithOAuthSubjectSecurityContextFactory implements WithSecurityContextFactory<WithOAuthSubject> {

    private DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();

    @Override
    public SecurityContext createSecurityContext(WithOAuthSubject withOAuthSubject) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // Copy of response from https://myidentityserver.com/identity/connect/accesstokenvalidation
        Map<String, ?> remoteToken = ImmutableMap.<String, Object>builder()
                .put("iss", "https://someidentify.example.com/identity")
                .put("aud", "oauth2-resource")
                .put("exp", OffsetDateTime.now().plusDays(1L).toEpochSecond() + "")
                .put("nbf", OffsetDateTime.now().plusDays(1L).toEpochSecond() + "")
                .put("client_id", "my-client-id")
                .put("scope", Arrays.asList(withOAuthSubject.scopes()))
                .put("sub", withOAuthSubject.subjectId())
                .put("auth_time", OffsetDateTime.now().toEpochSecond() + "")
                .put("idp", "idsrv")
                .put("amr", "password")
                .build();

        OAuth2Authentication authentication = defaultAccessTokenConverter.extractAuthentication(remoteToken);
        context.setAuthentication(authentication);
        return context;
    }
}