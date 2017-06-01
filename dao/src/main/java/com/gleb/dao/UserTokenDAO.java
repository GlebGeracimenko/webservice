package com.gleb.dao;

import java.util.UUID;

import com.gleb.dao.objects.DBToken;

public interface UserTokenDAO {

	void saveToken(DBToken dbToken);

	DBToken getTokenInfoForUser(UUID userId, String token);
	
	DBToken getValidToken(UUID userId);
}
