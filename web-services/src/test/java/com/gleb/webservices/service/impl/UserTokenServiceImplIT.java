package com.gleb.webservices.service.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import com.gleb.webservices.helpers.TimeHelper;
import com.gleb.webservices.test.fixtures.UserFixture;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gleb.dao.UserTokenDAO;
import com.gleb.dao.UsersDAO;
import com.gleb.dao.cassandra.BasicCQLCassandraDAO;
import com.gleb.dao.cassandra.CQLUserDAO;
import com.gleb.dao.cassandra.CQLUserTokenDAO;
import com.gleb.dao.objects.DBToken;
import com.gleb.dao.objects.TokenState;
import com.gleb.webservices.service.UserTokenService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class UserTokenServiceImplIT {

    @Autowired
    private BasicCQLCassandraDAO basicCassandraDAO;

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private UserTokenDAO userTokenDAO;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private TimeHelper timeHelper;

    public void cleanUp() {
        basicCassandraDAO.getSession().execute("truncate " + CQLUserTokenDAO.TOKEN_COLUMN_FAMILY);
        basicCassandraDAO.getSession().execute("truncate " + CQLUserDAO.USERS_COLUMN_FAMILY);
    }

    @After
    public void after() {
        cleanUp();
    }

    @Before
    public void before() {
        cleanUp();
    }

    @Test
    public void getValidTokenForUser_normalCase() throws Exception {
        UUID id = usersDAO.saveUser(UserFixture.createUser(UUID.randomUUID()));
        DBToken dbToken = new DBToken();
        dbToken.setCreatedAt(timeHelper.getCurrentTime());
        dbToken.setExpiredAt(timeHelper.getTimeFromNow(2, 0, 0));
        dbToken.setToken(RandomStringUtils.random(50, true, true));
        dbToken.setUserId(id);
        dbToken.setState(TokenState.VALID.name());
        userTokenDAO.saveToken(dbToken);

        String savedToken = userTokenService.getValidTokenForUser(id);
        assertThat(dbToken.getToken(), equalTo(savedToken));
    }

    @Test
    public void renewToken_normalCase() throws Exception {
        UUID id = usersDAO.saveUser(UserFixture.createUser(UUID.randomUUID()));
        DBToken dbToken1 = new DBToken();
        dbToken1.setCreatedAt(timeHelper.getCurrentTime());
        dbToken1.setExpiredAt(timeHelper.getTimeFromNow(2, 0, 0));
        dbToken1.setToken(RandomStringUtils.random(50, true, true));
        dbToken1.setUserId(id);
        dbToken1.setState(TokenState.VALID.name());
        userTokenDAO.saveToken(dbToken1);
        String oldToken = dbToken1.getToken();
        String savedToken = userTokenService.renewToken(id, dbToken1.getToken());
        assertThat(userTokenDAO.getValidToken(id).getToken(), equalTo(savedToken));

        DBToken oldTokenInfo = userTokenDAO.getTokenInfoForUser(id, oldToken);
        assertThat(oldTokenInfo.getState(), equalTo(TokenState.INVALID.name()));
    }

    @Test
    public void renewToken_expired() throws Exception {
        UUID id = usersDAO.saveUser(UserFixture.createUser(UUID.randomUUID()));
        DBToken dbToken1 = new DBToken();
        dbToken1.setCreatedAt(timeHelper.getTimeFromNow(-4, 0, 0));
        dbToken1.setExpiredAt(timeHelper.getTimeFromNow(-2, 0, 0));
        dbToken1.setToken(RandomStringUtils.random(50, true, true));
        dbToken1.setUserId(id);
        dbToken1.setState(TokenState.VALID.name());
        userTokenDAO.saveToken(dbToken1);
        String oldToken = dbToken1.getToken();
        String savedToken = userTokenService.renewToken(id, dbToken1.getToken());
        assertThat(userTokenDAO.getValidToken(id).getToken(), equalTo(savedToken));

        DBToken oldTokenInfo = userTokenDAO.getTokenInfoForUser(id, oldToken);
        assertThat(oldTokenInfo.getState(), equalTo(TokenState.INVALID.name()));
    }

    @Test
    public void isTokenExpired_normalCase() throws Exception {
        UUID id = usersDAO.saveUser(UserFixture.createUser(UUID.randomUUID()));
        DBToken dbToken1 = new DBToken();
        dbToken1.setCreatedAt(timeHelper.getTimeFromNow(-4, 0, 0));
        dbToken1.setExpiredAt(timeHelper.getTimeFromNow(-2, 0, 0));
        dbToken1.setToken(RandomStringUtils.random(50, true, true));
        dbToken1.setUserId(id);
        dbToken1.setState(TokenState.VALID.name());
        userTokenDAO.saveToken(dbToken1);
        boolean tokenExpired = userTokenService.isTokenExpired(id, dbToken1.getToken());
        assertTrue(tokenExpired);
        assertThat(userTokenDAO.getTokenInfoForUser(id, dbToken1.getToken()).getState(), equalTo(TokenState.EXPIRED.name()));
    }

    @Test
    public void isTokenValid_expired() throws Exception {
        UUID id = usersDAO.saveUser(UserFixture.createUser(UUID.randomUUID()));
        DBToken dbToken1 = new DBToken();
        dbToken1.setCreatedAt(timeHelper.getTimeFromNow(-4, 0, 0));
        dbToken1.setExpiredAt(timeHelper.getTimeFromNow(-2, 0, 0));
        dbToken1.setToken(RandomStringUtils.random(50, true, true));
        dbToken1.setUserId(id);
        dbToken1.setState(TokenState.VALID.name());
        userTokenDAO.saveToken(dbToken1);
        boolean tokenValid = userTokenService.isTokenValid(id, dbToken1.getToken());
        assertTrue(tokenValid);
        assertThat(userTokenDAO.getTokenInfoForUser(id, dbToken1.getToken()).getState(), equalTo(TokenState.VALID.name()));
    }

    @Test
    public void isTokenValid_normal() throws Exception {
        UUID id = usersDAO.saveUser(UserFixture.createUser(UUID.randomUUID()));
        DBToken dbToken1 = new DBToken();
        dbToken1.setCreatedAt(timeHelper.getCurrentTime());
        dbToken1.setExpiredAt(timeHelper.getTimeFromNow(5, 0, 0));
        dbToken1.setToken(RandomStringUtils.random(50, true, true));
        dbToken1.setUserId(id);
        dbToken1.setState(TokenState.VALID.name());
        userTokenDAO.saveToken(dbToken1);
        boolean tokenValid = userTokenService.isTokenValid(id, dbToken1.getToken());
        assertTrue(tokenValid);
        assertThat(userTokenDAO.getTokenInfoForUser(id, dbToken1.getToken()).getState(), equalTo(TokenState.VALID.name()));
    }

    @Test
    public void isTokenValid_userNotExists() throws Exception {
        UUID id = usersDAO.saveUser(UserFixture.createUser(UUID.randomUUID()));
        DBToken dbToken1 = new DBToken();
        dbToken1.setCreatedAt(timeHelper.getCurrentTime());
        dbToken1.setExpiredAt(timeHelper.getTimeFromNow(5, 0, 0));
        dbToken1.setToken(RandomStringUtils.random(50, true, true));
        dbToken1.setUserId(id);
        dbToken1.setState(TokenState.VALID.name());
        userTokenDAO.saveToken(dbToken1);
        basicCassandraDAO.getSession().execute("truncate " + CQLUserDAO.USERS_COLUMN_FAMILY);
        boolean tokenValid = userTokenService.isTokenValid(id, dbToken1.getToken());
        assertFalse(tokenValid);
        assertThat(userTokenDAO.getTokenInfoForUser(id, dbToken1.getToken()).getState(), equalTo(TokenState.INVALID.name()));
    }
}
