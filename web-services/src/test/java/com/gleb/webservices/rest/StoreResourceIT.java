package com.gleb.webservices.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

import com.gleb.webservices.test.fixtures.StoreFixture;
import com.gleb.webservices.test.fixtures.UserFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gleb.dao.UsersDAO;
import com.gleb.dao.cassandra.BasicCQLCassandraDAO;
import com.gleb.dao.cassandra.CQLUserDAO;
import com.gleb.dao.elasticsearch.ElasticStoreDAOImpl;
import com.gleb.dao.elasticsearch.ElasticsearchClient;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOStore;
import com.gleb.webservices.mapping.StoresMapper;
import com.gleb.webservices.tests.BasicIntegrationTest;

/**
 * Created by gleb on 14.09.15.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({@ContextConfiguration("/testApplicationContext.xml")})
public class StoreResourceIT extends BasicIntegrationTest {

    @Autowired
    private ElasticStoreDAOImpl elasticStoreDAO;

    @Autowired
    private ElasticsearchClient clientHolder;

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private BasicCQLCassandraDAO basicCQLDAO;

    @Autowired
    private StoresMapper storesMapper;

    private DBUser testUser = UserFixture.createUserImporter(UUID.randomUUID());

    private void cleanUp() {

        basicCQLDAO.getSession().execute("truncate " + CQLUserDAO.USERS_COLUMN_FAMILY);

//        clientHolder.getClient().prepareDeleteByQuery(clientHolder.getIndexName()).setQuery(QueryBuilders.matchAllQuery())
//                .setTypes(ElasticStoreDAOImpl.STORE_TYPE).execute().actionGet();
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
    public void test() throws InterruptedException {
        BOStore boStore = StoreFixture.createStore(null, 15L);
        Thread.sleep(2000);
        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
        Entity<BOStore> entity = Entity.entity(boStore, MediaType.APPLICATION_JSON_VALUE);
        Response response = path("/stores").request().cookie("JSESSIONID", cookie.getValue()).post(entity);
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
        logout(cookie);
    }
}
