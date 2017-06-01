package com.gleb.webservices.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.gleb.webservices.helpers.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gleb.dao.UsersDAO;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.service.IdsService;
import com.gleb.webservices.service.UserService;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private IdsService idsService;

    @Override
    public DBUser getDBUserByFaceBookId(String token) {
        FacebookClient facebookClient = new DefaultFacebookClient(token);
        User user = facebookClient.fetchObject("me", User.class);
        DBUser dbUser = usersDAO.getUserBySocialId(UsersDAO.SOCIAL_NETWORK_FACEBOOK, user.getId());
        if (dbUser == null) {
            dbUser = new DBUser();
            dbUser.setCreatedAt(new Date());
            dbUser.setDeleted(false);
            dbUser.setEmail(user.getEmail());
            if (user.getGender() == null) {
                dbUser.setGender(DBUser.GENDER_FEMALE);
            } else {
                dbUser.setGender(user.getGender().equalsIgnoreCase("male") ? DBUser.GENDER_MALE : DBUser.GENDER_FEMALE);
            }
            dbUser.setGroups(Arrays.asList(UserRoles.ROLE_USER));
            dbUser.setId(idsService.getNewId());
            dbUser.setLogin(user.getEmail());
            dbUser.setName(user.getFirstName());
            dbUser.setPassword(null);
            Map<String, String> socialIds = new HashMap<String, String>();
            socialIds.put(UsersDAO.SOCIAL_NETWORK_FACEBOOK, user.getId());
            dbUser.setSocialIds(socialIds);

            Map<String, String> socialTokens = new HashMap<String, String>();
            socialTokens.put(UsersDAO.SOCIAL_NETWORK_FACEBOOK, token);
            dbUser.setSocialTokens(socialTokens);
            usersDAO.saveUser(dbUser);
        }
        return dbUser;
    }

    @Override
    public DBUser getDBUserByLogin(String login) {
        return usersDAO.getUserByLogin(login);
    }

    @Override
    public DBUser getDBUserByEmail(String email) {
        return usersDAO.getUserByEmail(email);
    }

    @Override
    public DBUser findUserByLoginAndEmail(String login, String email) {
        DBUser dbUserByEmail = usersDAO.getUserByEmail(email);
        if (dbUserByEmail != null) {
            return dbUserByEmail;
        }
        return usersDAO.getUserByLogin(login);
    }

    @Override
    public UUID saveUser(DBUser dbUser) {
        if(dbUser.getId() == null) {
            UUID id = idsService.getNewId();
            dbUser.setId(id);
        }
        usersDAO.saveUser(dbUser);
        return dbUser.getId();
    }

	@Override
	public DBUser getDBUserById(UUID id) {
		return usersDAO.getUserById(id);
	}
}
