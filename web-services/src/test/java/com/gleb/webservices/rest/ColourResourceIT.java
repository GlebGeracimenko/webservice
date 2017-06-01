package com.gleb.webservices.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gleb.webservices.bo.BOColour;
import com.gleb.webservices.bo.BOColourCombination;
import com.gleb.webservices.mapping.ColourCombinationMapper;
import com.gleb.webservices.mapping.ColourMapper;
import com.gleb.webservices.test.fixtures.UserFixture;
import com.gleb.webservices.tests.BasicIntegrationTest;
import com.gleb.dao.ColourCombinationsDAO;
import com.gleb.dao.ColourDAO;
import com.gleb.dao.UsersDAO;
import com.gleb.dao.cassandra.CQLUserDAO;
import com.gleb.dao.elasticsearch.ElasticColourCombinationDAOImpl;
import com.gleb.dao.elasticsearch.ElasticColourDAOImpl;
import com.gleb.dao.objects.DBColour;
import com.gleb.dao.objects.DBColourCombination;
import com.gleb.dao.objects.DBUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

/**
 * Created by gleb on 03.12.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({@ContextConfiguration("/testApplicationContext.xml")})
public class ColourResourceIT extends BasicIntegrationTest {

    @Autowired
    private ColourDAO colourDAO;

    @Autowired
    private ColourMapper colourMapper;

    @Autowired
    private ColourCombinationsDAO combinationsDAO;

    @Autowired
    private ColourCombinationMapper colourCombinationMapper;

    private ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private UsersDAO usersDAO;

    private DBUser testUser = UserFixture.createUserImporter(UUID.randomUUID());

    private void cleanUp() {
        truncateCassandra(CQLUserDAO.USERS_COLUMN_FAMILY);
        truncateElasticsearchType(ElasticColourDAOImpl.COLOUR_TYPE);
        truncateElasticsearchType(ElasticColourCombinationDAOImpl.COLOUR_COMBINATIONS_TYPE);
    }

//    @After
//    public void after() {
//        cleanUp();
//    }

    @Before
    public void before() {
        cleanUp();
        usersDAO.saveUser(testUser);
    }

    private DBColour createColour(String value) throws IOException {
        return jsonMapper.readValue(value, DBColour.class);
    }

    private void check(BOColour colour1, BOColour colour2) {
        assertThat(colour1.getId(), equalTo(colour2.getId()));
        assertThat(colour1.getName(), equalTo(colour2.getName()));
        assertThat(colour1.getGroup(), equalTo(colour2.getGroup()));
        assertThat(colour1.getColours().size(), equalTo(colour2.getColours().size()));
        for (int i = 0; i < colour1.getColours().size(); i++) {
            assertThat(colour1.getColours().get(i), equalTo(colour2.getColours().get(i)));
        }
        assertThat(colour1.getExampleColour().getBlue(), equalTo(colour2.getExampleColour().getBlue()));
        assertThat(colour1.getExampleColour().getGreen(), equalTo(colour2.getExampleColour().getGreen()));
        assertThat(colour1.getExampleColour().getRed(), equalTo(colour2.getExampleColour().getRed()));
    }

    private void check(BOColourCombination combination1, BOColourCombination combination2) {
        assertThat(combination1.getId(), equalTo(combination2.getId()));
        assertThat(combination1.getMainColourName(), equalTo(combination2.getMainColourName()));
        assertThat(combination1.getSecondColourName(), equalTo(combination2.getSecondColourName()));
        assertThat(combination1.getCombinationName(), equalTo(combination2.getCombinationName()));
        assertThat(combination1.getCombinedColourIds().size(), equalTo(combination2.getCombinedColourIds().size()));
        for (int i = 0; i < combination1.getCombinedColourIds().size(); i++) {
            assertThat(combination1.getCombinedColourIds().get(i), equalTo(combination2.getCombinedColourIds().get(i)));
        }
    }

    @Test
    public void test() throws IOException, InterruptedException {
        DBColour dbColour = createColour("{\"id\" : \"0\", \"name\":\"indianred\", \"exampleColour\" : {\"red\":255,\"green\":135,\"blue\":135}, \"colours\" : [\"indianred\", \"lightpink\", \"salmon\", \"mistyrose\", \"lightcoral\", \"pink\"], \"group\":\"\"}");
        BOColour boColour = colourMapper.map(dbColour);
        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
        Entity<BOColour> entity = Entity.entity(boColour, MediaType.APPLICATION_JSON);

        //save
        Response response = path("/colour").request().cookie("JSESSIONID", cookie.getValue()).post(entity);
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
        Thread.sleep(1000);
        assertThat(colourDAO.getAll().size(), equalTo(1));

        //get
        BOColour getColor = path("/colour/" + boColour.getId()).request().cookie("JSESSIONID", cookie.getValue()).get(BOColour.class);
        assertNotNull(getColor);
        check(getColor, boColour);

        dbColour = createColour("{\"id\" : 1, \"name\":\"red\", \"exampleColour\" : {\"red\":255,\"green\":0,\"blue\": 0}, \"colours\" : [\"red\", \"orangered\", \"tomato\", \"freespeechred\"], \"group\":\"\"}");
        boColour = colourMapper.map(dbColour);
        entity = Entity.entity(boColour, MediaType.APPLICATION_JSON);

        //save
        response = path("/colour").request().cookie("JSESSIONID", cookie.getValue()).post(entity);
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
        Thread.sleep(1000);
        assertThat(colourDAO.getAll().size(), equalTo(2));

        //get
        getColor = path("/colour/" + boColour.getId()).request().cookie("JSESSIONID", cookie.getValue()).get(BOColour.class);
        assertNotNull(getColor);
        check(getColor, boColour);

        DBColourCombination dbColourCombination = new DBColourCombination();
        dbColourCombination.setId("0");
        dbColourCombination.setMainColourName("red");
        dbColourCombination.setSecondColourName("indianred111");
        dbColourCombination.setCombinationName("indianred");
        dbColourCombination.setCombinedColourIds(Arrays.asList("0", "1"));

        combinationsDAO.save(dbColourCombination);

        //get
        Thread.sleep(1000);
        Collection<BOColourCombination> combinations = path("/colour/combinations").request()
                .cookie("JSESSIONID", cookie.getValue())
                .get(new GenericType<Collection<BOColourCombination>>() {
                });
        assertNotNull(combinations);
        assertThat(combinations.size(), equalTo(1));
        check(combinations.iterator().next(), colourCombinationMapper.map(dbColourCombination));

        combinations = path("/colour/combinations/" + dbColourCombination.getMainColourName()).request()
                .cookie("JSESSIONID", cookie.getValue())
                .get(new GenericType<Collection<BOColourCombination>>() {
                });
        assertNotNull(combinations);
        assertThat(combinations.size(), equalTo(1));
        check(combinations.iterator().next(), colourCombinationMapper.map(dbColourCombination));

        BOColourCombination colourCombination = path("/colour/combinations/" + dbColourCombination.getMainColourName() +
                "/" + dbColourCombination.getCombinationName()).request()
                .cookie("JSESSIONID", cookie.getValue())
                .get(BOColourCombination.class);
        assertNotNull(colourCombination);
        check(colourCombination, colourCombinationMapper.map(dbColourCombination));

        DBColourCombination dbColourCombination1 = new DBColourCombination();
        dbColourCombination1.setId("1");
        dbColourCombination1.setMainColourName("red");
        dbColourCombination1.setSecondColourName("indianred111");
        dbColourCombination1.setCombinationName("indianred1");
        dbColourCombination1.setCombinedColourIds(Arrays.asList("0", "2"));

        combinationsDAO.save(dbColourCombination1);

        //get
        Thread.sleep(1000);
        combinations = path("/colour/combinations").request()
                .cookie("JSESSIONID", cookie.getValue())
                .get(new GenericType<Collection<BOColourCombination>>() {
                });
        assertNotNull(combinations);
        assertThat(combinations.size(), equalTo(2));
        List<BOColourCombination> combinationList = new ArrayList<>(combinations);
        for (BOColourCombination combination : combinationList) {
            if (combination.getId().equals("0")) {
                check(combination, colourCombinationMapper.map(dbColourCombination));
            } else if (combination.getId().equals("1")) {
                check(combination, colourCombinationMapper.map(dbColourCombination1));
            }
        }

        combinations = path("/colour/combinations/" + dbColourCombination1.getMainColourName()).request()
                .cookie("JSESSIONID", cookie.getValue())
                .get(new GenericType<Collection<BOColourCombination>>() {
                });
        assertNotNull(combinations);
        assertThat(combinations.size(), equalTo(2));
        combinationList = new ArrayList<>(combinations);
        for (BOColourCombination combination : combinationList) {
            if (combination.getId().equals("0")) {
                check(combination, colourCombinationMapper.map(dbColourCombination));
            } else if (combination.getId().equals("1")) {
                check(combination, colourCombinationMapper.map(dbColourCombination1));
            }
        }

        colourCombination = path("/colour/combinations/" + dbColourCombination1.getMainColourName() +
                "/" + dbColourCombination1.getCombinationName()).request()
                .cookie("JSESSIONID", cookie.getValue())
                .get(BOColourCombination.class);
        assertNotNull(colourCombination);
        check(colourCombination, colourCombinationMapper.map(dbColourCombination1));

        logout(cookie);

    }

}
