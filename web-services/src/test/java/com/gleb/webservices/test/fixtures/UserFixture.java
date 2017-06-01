package com.gleb.webservices.test.fixtures;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import com.gleb.webservices.helpers.UserRoles;
import com.gleb.dao.objects.DBUser;

public class UserFixture {
    public static DBUser createUser(UUID id) {
        DBUser user = new DBUser();
        user.setCreatedAt(new Date());
        user.setDeleted(false);
        user.setEmail("someemail@example.com");
        user.setGender(DBUser.GENDER_FEMALE);
        user.setLogin("test");
        user.setPassword("test");
        user.setId(id);
        user.setGroups(Arrays.asList(UserRoles.ROLE_USER));
        return user;
    }

    public static DBUser createUserImporter(UUID id) {
        DBUser user = new DBUser();
        user.setCreatedAt(new Date());
        user.setDeleted(false);
        user.setEmail("someemail@example.com");
        user.setGender(DBUser.GENDER_FEMALE);
        user.setLogin("test");
        user.setPassword("test");
        user.setId(id);
        user.setGroups(Arrays.asList(UserRoles.ROLE_IMPORTER, UserRoles.ROLE_USER));
        return user;
    }
}
