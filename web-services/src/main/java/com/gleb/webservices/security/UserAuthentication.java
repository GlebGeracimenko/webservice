package com.gleb.webservices.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.gleb.dao.objects.DBUser;

public class UserAuthentication extends AbstractAuthenticationToken {
    public UserAuthentication(DBUser user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        setDetails(user);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        final DBUser user = (DBUser) getDetails();
        if (user == null) {
            return null;
        }
        return new UserDetailsImpl(user);
    }
}
