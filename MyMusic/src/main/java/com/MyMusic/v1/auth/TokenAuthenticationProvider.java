package com.MyMusic.v1.auth;

import com.MyMusic.v1.services.AuthService;
import com.MyMusic.v1.services.models.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;
@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired
    private AuthService authService;

    @Override
    protected void additionalAuthenticationChecks(
            final UserDetails d, final UsernamePasswordAuthenticationToken auth) {
    }

    @Override
    protected UserDetails retrieveUser(
            final String username,
            final UsernamePasswordAuthenticationToken authentication) {
        final String token = (String) authentication.getCredentials();
        Optional<Credentials> creds =
                authService.getCredentialsByAuthToken(token);

        return creds.map((c) ->
                        new MyUser(c.id, c.username, "",
                                c.roles.stream().map(Authorities::fromRole)
                                        .collect(Collectors.toList()))).
                orElseThrow(() -> new BadCredentialsException("user not found"));
    }
}
