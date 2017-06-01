package com.gleb.webservices.helpers;

import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.security.UserAuthentication;

@Component
public class SecurityHelper {

	public DBUser getCurrentUser(SecurityContext securityContext) {
		UserAuthentication userAuth = (UserAuthentication) securityContext.getUserPrincipal();
		return (DBUser) userAuth.getDetails();
	}
	
	public int getGenderForCurrentUser(SecurityContext securityContext) {
		return getCurrentUser(securityContext).getGender();
	}
}
