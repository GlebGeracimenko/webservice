package com.gleb.webservices.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gleb.dao.UsersDAO;
import com.gleb.dao.cassandra.CQLUserDAO;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.LoginPassword;
import com.gleb.webservices.tests.BasicIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class AuthResourceIT extends BasicIntegrationTest {

    @Autowired
    private UsersDAO usersDAO;

    private void cleanUp() throws IOException {
        truncateCassandra(CQLUserDAO.USERS_COLUMN_FAMILY);
        truncateElasticsearchType("products");
    }

    @After
    public void after() throws IOException {
        cleanUp();
    }

    @Before
    public void before() throws IOException {
        cleanUp();
    }

    @Test
    public void testRegister_normalCase() throws IOException {
        LoginPassword loginPassword = new LoginPassword();
        loginPassword.setEmail("keebraa@gmail.com");
        loginPassword.setLogin("123");
        loginPassword.setPasswordBase64("123");
        Entity<LoginPassword> entity = Entity.entity(loginPassword, MediaType.APPLICATION_JSON_VALUE);

        Response response = path("/auth/register").request().post(entity);
        assertThat(response.getStatus(), equalTo(Status.OK.getStatusCode()));
        DBUser dbUser = usersDAO.getUserByEmail(loginPassword.getEmail());
        assertThat(dbUser.getEmail(), equalTo(loginPassword.getEmail()));
    }
}
