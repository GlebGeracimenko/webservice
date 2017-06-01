package com.gleb.webservices.service.impl;

import java.util.UUID;

import com.gleb.webservices.helpers.TimeHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gleb.dao.UserTokenDAO;
import com.gleb.dao.objects.DBToken;
import com.gleb.dao.objects.DBUser;
import com.gleb.dao.objects.TokenState;
import com.gleb.webservices.service.UserService;
import com.gleb.webservices.service.UserTokenService;

@Service
public class UserTokenServiceImpl implements UserTokenService {

	@Value("${services.token.size}")
	private int tokenSize;

	@Value("${services.token.lifetimeInDays}")
	private int daysAlive;

	@Autowired
	private UserTokenDAO userTokenDAO;

	@Autowired
	private UserService userService;

	@Autowired
	private TimeHelper timeHelper;

	@Override
	public String createTokenForUser(UUID id) {
		DBToken dbToken = new DBToken();
		dbToken.setCreatedAt(timeHelper.getCurrentTime());
		dbToken.setExpiredAt(timeHelper.getTimeFromNow(daysAlive, 0, 0));
		dbToken.setState(TokenState.VALID.name());
		dbToken.setToken(generateNewToken());
		dbToken.setUserId(id);
		userTokenDAO.saveToken(dbToken);
		return dbToken.getToken();
	}

	private String generateNewToken() {
		return RandomStringUtils.random(tokenSize, true, true);
	}

	@Override
	public DBUser getUserByToken(UUID userId, String token) {
		DBToken dbToken = userTokenDAO.getTokenInfoForUser(userId, token);
		if (dbToken == null) {
			return null;
		}
		TokenState state = TokenState.valueOf(dbToken.getState());
		if (state == TokenState.INVALID) {
			return null;
		}
		return userService.getDBUserById(dbToken.getUserId());
	}

	@Override
	public boolean isTokenExpired(UUID userId, String token) {
		DBUser dbUser = userService.getDBUserById(userId);
		if (dbUser == null) {
			return true;
		}
		DBToken dbToken = userTokenDAO.getTokenInfoForUser(userId, token);
		if (dbToken == null) {
			return true;
		}
		TokenState state = TokenState.valueOf(dbToken.getState());
		if (state == TokenState.EXPIRED) {
			return true;
		}
		boolean isExpired = timeHelper.getCurrentTime().after(dbToken.getExpiredAt());
		if (isExpired) {
			dbToken.setState(TokenState.EXPIRED.name());
			userTokenDAO.saveToken(dbToken);
		}
		return isExpired;
	}

	@Override
	public String renewToken(UUID userId, String oldToken) {
		DBToken dbToken = userTokenDAO.getTokenInfoForUser(userId, oldToken);
		if (dbToken == null) {
			return null;
		}
		TokenState currentState = TokenState.valueOf(dbToken.getState());
		if (currentState == null || currentState.equals(TokenState.INVALID)) {
			return null;
		}
		DBUser dbUser = userService.getDBUserById(userId);
		if (dbUser == null) {
			return null;
		}
		dbToken.setState(TokenState.INVALID.name());
		String newToken = generateNewToken();
		DBToken newDBToken = new DBToken();
		newDBToken.setCreatedAt(timeHelper.getCurrentTime());
		newDBToken.setExpiredAt(timeHelper.getTimeFromNow(daysAlive, 0, 0));
		newDBToken.setUserId(userId);
		newDBToken.setToken(newToken);
		newDBToken.setState(TokenState.VALID.name());
		userTokenDAO.saveToken(newDBToken);
		userTokenDAO.saveToken(dbToken);
		return newToken;
	}

	@Override
	public void invalidateToken(UUID userId, String token) {
		DBToken dbToken = userTokenDAO.getTokenInfoForUser(userId, token);
		if (dbToken == null) {
			return;
		}
		TokenState currentState = TokenState.valueOf(dbToken.getState());
		if (currentState == null || currentState.equals(TokenState.INVALID)) {
			return;
		}
		DBUser dbUser = userService.getDBUserById(userId);
		if (dbUser == null) {
			return;
		}
		dbToken.setState(TokenState.INVALID.name());
		userTokenDAO.saveToken(dbToken);
	}

	@Override
	public boolean isTokenExists(UUID userId, String token) {
		DBToken dbToken = userTokenDAO.getTokenInfoForUser(userId, token);
		if (dbToken == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isTokenValid(UUID userId, String token) {
		DBToken dbToken = userTokenDAO.getTokenInfoForUser(userId, token);
		if (dbToken == null) {
			return false;
		}
		TokenState state = TokenState.valueOf(dbToken.getState());
		if (state == TokenState.INVALID) {
			return false;
		}
		DBUser dbUser = userService.getDBUserById(userId);
		if (dbUser == null) {
			dbToken.setState(TokenState.INVALID.name());
			userTokenDAO.saveToken(dbToken);
			return false;
		}
		return true;
	}

	@Override
	public String getValidTokenForUser(UUID id) {
	    
		DBToken dbToken = userTokenDAO.getValidToken(id);
		if (dbToken == null) {
			return null;
		}
		if(userService.getDBUserById(id) == null) {
		    dbToken.setState(TokenState.INVALID.name());
            userTokenDAO.saveToken(dbToken);
            return null;
        }
		return dbToken.getToken();
	}

    @Override
    public DBToken getTokenInfo(UUID userId, String token) {
        return userTokenDAO.getTokenInfoForUser(userId, token);
    }
}
