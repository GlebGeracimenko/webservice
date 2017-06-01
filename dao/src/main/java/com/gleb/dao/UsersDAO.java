package com.gleb.dao;

import java.util.UUID;

import com.gleb.dao.objects.DBAdminUser;
import com.gleb.dao.objects.DBUser;

public interface UsersDAO {
	public static final String SOCIAL_NETWORK_FACEBOOK = "FB";
    DBUser getUserByLogin(String login);

    DBUser getUserById(UUID id);

    DBUser getUserByEmail(String email);
    
    UUID saveUser(DBUser user);
    
    DBUser getUserBySocialId(String socialNetwork, String id);
    
    DBAdminUser getAdminUserByLogin(String login);
}
