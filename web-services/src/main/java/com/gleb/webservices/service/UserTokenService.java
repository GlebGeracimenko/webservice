package com.gleb.webservices.service;

import java.util.UUID;

import com.gleb.dao.objects.DBToken;
import com.gleb.dao.objects.DBUser;

public interface UserTokenService {

	String createTokenForUser(UUID userId);
	
	DBUser getUserByToken(UUID userId, String token);
	
	boolean isTokenExpired(UUID userId, String token);
	
	String renewToken(UUID userId, String oldToken);
	
	void invalidateToken(UUID userId, String token);
	
	boolean isTokenExists(UUID userId, String token);
	
	boolean isTokenValid(UUID userId, String token);
	
	DBToken getTokenInfo(UUID userId, String token);
	
	String getValidTokenForUser(UUID id);
	
}
