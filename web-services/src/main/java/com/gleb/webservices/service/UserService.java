package com.gleb.webservices.service;

import java.util.UUID;

import com.gleb.dao.objects.DBUser;

public interface UserService {
    public DBUser getDBUserByFaceBookId(String token);

    public DBUser getDBUserByLogin(String login);
    
    public DBUser getDBUserById(UUID id);

    public DBUser getDBUserByEmail(String email);
    
    public DBUser findUserByLoginAndEmail(String login, String email);
    
    public UUID saveUser(DBUser dbUser);
}
