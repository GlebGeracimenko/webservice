package com.gleb.webservices.service;

import javax.ws.rs.core.SecurityContext;

import com.gleb.dao.objects.DBUser;

public interface AuthService {
    DBUser getCurrentUser(SecurityContext securityContext);
    void authorizeUser(DBUser user);
}
