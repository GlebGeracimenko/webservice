package com.gleb.webservices.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.gleb.webservices.mapping.BrandsMapper;
import com.gleb.webservices.test.fixtures.UserFixture;
import com.gleb.webservices.tests.BasicIntegrationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gleb.dao.FollowsDAO;
import com.gleb.dao.UsersDAO;
import com.gleb.dao.cassandra.BasicCQLCassandraDAO;
import com.gleb.dao.cassandra.CQLFollowDAOImpl;
import com.gleb.dao.cassandra.CQLUserDAO;
import com.gleb.dao.elasticsearch.ElasticBrandsDAOImpl;
import com.gleb.dao.elasticsearch.ElasticsearchClient;
import com.gleb.dao.objects.DBBrand;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.test.fixtures.BrandFixture;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class BrandsResourceIT extends BasicIntegrationTest {

    @Autowired
    private ElasticBrandsDAOImpl brandsDAO;

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private BasicCQLCassandraDAO basicCQLDAO;

    @Autowired
    private ElasticsearchClient clientHolder;

    @Autowired
    private BrandsMapper brandsMapper;

    @Autowired
    private FollowsDAO followsDAO;

    private DBUser testUser = UserFixture.createUser(UUID.randomUUID());

    private void cleanUp() {

        basicCQLDAO.getSession().execute("truncate " + CQLUserDAO.USERS_COLUMN_FAMILY);
        basicCQLDAO.getSession().execute("truncate " + CQLFollowDAOImpl.FOLLOWS_USERS_COLUMN_FAMILY);
        basicCQLDAO.getSession().execute("truncate " + CQLFollowDAOImpl.USERS_FOLLOWS_COLUMN_FAMILY);
        truncateElasticsearchType(ElasticBrandsDAOImpl.BRAND_TYPE);
    }

    @After
    public void after() {
        cleanUp();
    }

    @Before
    public void before() {
        cleanUp();
        usersDAO.saveUser(testUser);
    }

    @Test
    public void test_follow_normalCase() throws JsonProcessingException {
        DBBrand dbBrand = brandsMapper.map(BrandFixture.createBrand(UUID.randomUUID().toString()));
        brandsDAO.saveBrand(dbBrand);
        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
        Response response = path("/brands/follow/"+dbBrand.getId()).request().cookie("JSESSIONID", cookie.getValue()).post(null);
        assertThat(response.getStatus(), equalTo(Status.OK.getStatusCode()));
        Collection<String> followIds = followsDAO.getAllFollowedObjectsByUser(testUser.getId(),FollowsDAO.TYPE_BRAND);
        assertThat(followIds.size(), equalTo(1));
        assertThat(followIds.iterator().next(), equalTo(dbBrand.getId()));
        logout(cookie);
    }
    
    @Test
    public void test_follow_wrongBrand() throws JsonProcessingException {
        DBBrand dbBrand = brandsMapper.map(BrandFixture.createBrand(UUID.randomUUID().toString()));
        brandsDAO.saveBrand(dbBrand);
        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
        Response response = path("/brands/follow/"+UUID.randomUUID()).request().cookie(cookie).post(null);
        assertThat(response.getStatus(), equalTo(Status.BAD_REQUEST.getStatusCode()));
        Collection<String> followIds = followsDAO.getAllFollowedObjectsByUser(testUser.getId(), FollowsDAO.TYPE_BRAND);
        assertThat(followIds.size(), equalTo(0));
        logout(cookie);
    }
    
    @Test
    public void test_unfollow_normalCase() throws JsonProcessingException {
        DBBrand dbBrand = brandsMapper.map(BrandFixture.createBrand(UUID.randomUUID().toString()));
        brandsDAO.saveBrand(dbBrand);
        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
        Response response = path("/brands/follow/"+dbBrand.getId()).request().cookie(cookie).post(null);
        assertThat(response.getStatus(), equalTo(Status.OK.getStatusCode()));
        Collection<String> followIds = followsDAO.getAllFollowedObjectsByUser(testUser.getId(), FollowsDAO.TYPE_BRAND);
        assertThat(followIds.size(), equalTo(1));
        assertThat(followIds.iterator().next(), equalTo(dbBrand.getId()));
        
        response = path("/brands/unfollow/"+dbBrand.getId()).request().cookie("JSESSIONID", cookie.getValue()).post(null);
        assertThat(response.getStatus(), equalTo(Status.OK.getStatusCode()));
        followIds = followsDAO.getAllFollowedObjectsByUser(testUser.getId(), FollowsDAO.TYPE_BRAND);
        assertThat(followIds.size(), equalTo(0));
        logout(cookie);
    }
}
