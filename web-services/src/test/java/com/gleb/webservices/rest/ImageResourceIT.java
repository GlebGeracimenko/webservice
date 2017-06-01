package com.gleb.webservices.rest;

//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.junit.Assert.assertThat;
//
//import UsersDAO;
//import BasicCQLCassandraDAO;
//import CQLFollowDAOImpl;
//import CQLImagesDAO;
//import CQLUserDAO;
//import DBUser;
//import BOImage;
//import UserFixture;
//import BasicIntegrationTest;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.ContextHierarchy;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.Cookie;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;

import com.gleb.webservices.tests.BasicIntegrationTest;

/**
 * Created by gleb on 27.10.15.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ImageResourceIT extends BasicIntegrationTest {

//    @Autowired
//    private UsersDAO usersDAO;
//
//    @Autowired
//    private BasicCQLCassandraDAO basicCQLDAO;
//
//    private DBUser testUser = UserFixture.createUserImporter(1L);
//
//    @SuppressWarnings("deprecation")
//    private void cleanUp() {
//        basicCQLDAO.getSession().execute("truncate " + CQLUserDAO.USERS_COLUMN_FAMILY);
//        basicCQLDAO.getSession().execute("truncate " + CQLFollowDAOImpl.FOLLOWS_USERS_COLUMN_FAMILY);
//        basicCQLDAO.getSession().execute("truncate " + CQLFollowDAOImpl.USERS_FOLLOWS_COLUMN_FAMILY);
//        basicCQLDAO.getSession().execute("truncate " + CQLImagesDAO.IMAGES_TABLE);
//    }
//
////    @After
////    public void after() {
////        cleanUp();
////    }
//
//    @Before
//    public void before() {
//        cleanUp();
//        usersDAO.saveUser(testUser);
//    }
//
//    @Test
//    public void test() {
//        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
//        Response response = path("/image/save").request().post(Entity.entity(createImage(), MediaType.APPLICATION_JSON));
//        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
//    }
//
//    private BOImage createImage() {
//        BOImage boImage = new BOImage();
//        boImage.setId(1L);
//        boImage.setInternal_name("TestRest");
//        boImage.setInternal_link("TestRest");
//        boImage.setExternal_link("TestRest");
//        boImage.setHoster("TestRest");
//        return boImage;
//    }

}
