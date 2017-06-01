package com.gleb.webservices.service.impl;

import java.util.List;

import javax.ws.rs.core.SecurityContext;

import com.gleb.webservices.helpers.UserHelper;
import com.gleb.webservices.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.security.UserAuthentication;

@Service
public class AuthServiceImpl implements AuthService {

    public DBUser getCurrentUser(SecurityContext securityContext) {
        UserAuthentication userAuth = (UserAuthentication) securityContext.getUserPrincipal();
        if (userAuth == null) {
            return null;
        }
        return (DBUser) userAuth.getDetails();
    }

    public void authorizeUser(DBUser user) {
        List<GrantedAuthority> authorities = UserHelper.getAuthorities(user);
        Authentication a = new UserAuthentication(user, authorities);
        SecurityContextHolder.getContext().setAuthentication(a);
    }
}
