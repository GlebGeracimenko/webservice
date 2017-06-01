package com.gleb.webservices.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.UUID;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.gleb.webservices.test.fixtures.UserFixture;
import com.gleb.webservices.tests.BasicIntegrationTest;
import com.gleb.dao.LikesDAO;
import com.gleb.dao.cassandra.CQLLikesDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gleb.dao.UsersDAO;
import com.gleb.dao.cassandra.BasicCQLCassandraDAO;
import com.gleb.dao.cassandra.CQLUserDAO;
import com.gleb.dao.elasticsearch.ElasticProductDAOImpl;
import com.gleb.dao.elasticsearch.ElasticsearchClient;
import com.gleb.dao.objects.DBProduct;
import com.gleb.dao.objects.DBUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ProductResourceIT extends BasicIntegrationTest {

    @Autowired
    private ElasticProductDAOImpl itemsDAO;

    @Autowired
    private LikesDAO likesDAO;

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private BasicCQLCassandraDAO basicCQLDAO;
    
    @Autowired
    private ElasticsearchClient clientHolder;
    
    private DBUser testUser = UserFixture.createUser(UUID.randomUUID());
    private DBUser testUser1 = UserFixture.createUser(UUID.randomUUID());

    private void cleanUp() {
        truncateCassandra(CQLUserDAO.USERS_COLUMN_FAMILY);
        truncateCassandra(CQLLikesDAO.LIKES_COLUMN_FAMILY);
        truncateElasticsearchType(ElasticProductDAOImpl.PRODUCT_TYPE);
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

    private DBProduct createItem(String id) {
        DBProduct item = new DBProduct();
        item.setBrand("some brand");
        item.setCategory("some category");
        item.setColor(Arrays.asList("white"));
        item.setDesc("description");
        item.setGender(DBUser.GENDER_MALE);
        item.setImages(Arrays.asList("image.jpg"));
        item.setName("some name");
        item.setPrice(10.0);
        item.setSize(Arrays.asList("XL"));
        item.setSku("123");
        item.setStore("FC");
        item.setId(id);
        item.setUrl("http://example.com/item");
        return item;
    }


    private void like() throws JsonProcessingException, InterruptedException {
        String id = itemsDAO.saveProduct(createItem(UUID.randomUUID().toString()));
        Thread.sleep(1000);
        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
        Response response = path("/products/like/" + id).request().cookie(cookie).get();
        assertThat(response.getStatus(), equalTo(Status.OK.getStatusCode()));
        assertThat(likesDAO.getLikesCountForProduct(id), equalTo(1L));
        logout(cookie);
        testUser1.setLogin("login1");
        testUser1.setEmail("login1@123.123");
        usersDAO.saveUser(testUser1);
        Cookie cookie1 = login(testUser1.getLogin(), testUser1.getPassword());
        response = path("/products/like/" + id).request().cookie(cookie1).get();
        assertThat(response.getStatus(), equalTo(Status.OK.getStatusCode()));
        Long aLong = likesDAO.getLikesCountForProduct(id);
        assertThat(aLong, equalTo(2L));
        logout(cookie);
    }

    private void dislike() throws JsonProcessingException {
        String id = itemsDAO.saveProduct(createItem(UUID.randomUUID().toString()));
        Cookie cookie = login(testUser1.getLogin(), testUser1.getPassword());
        Response response = path("/products/dislike/"+id).request().cookie(cookie).get();
        assertThat(response.getStatus(), equalTo(Status.OK.getStatusCode()));
        assertThat(likesDAO.getDislikesCountForProduct(id), equalTo(1L));
        logout(cookie);
    }

    @Test
    public void test() throws JsonProcessingException, InterruptedException {
        like();
        dislike();
    }
}
