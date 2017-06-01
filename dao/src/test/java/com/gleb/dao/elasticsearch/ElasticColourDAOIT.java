package com.gleb.dao.elasticsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.gleb.dao.ColourDAO;
import com.gleb.dao.fixture.DBColourFixture;
import com.gleb.dao.objects.DBColour;
import org.elasticsearch.action.get.GetResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ElasticColourDAOIT extends ElasticAbstractIT {

    @Autowired
    private ColourDAO colourDAO;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Before
    public void before() {
        truncateType(ElasticColourDAOImpl.COLOUR_TYPE);
    }

    @After
    public void after() {
        truncateType(ElasticColourDAOImpl.COLOUR_TYPE);
    }

    @Test
    public void save_normalCase() throws IOException {
        DBColour colour = DBColourFixture.create("1L");
        colourDAO.save(colour);
        GetResponse response = elasticsearchClient.getClient().prepareGet().setId("1L").setIndex(elasticsearchClient.getIndexName())
                .setType(ColourDAO.COLOUR_TYPE).get();
        DBColour savedDBColour = new ObjectMapper().readValue(response.getSourceAsString(), DBColour.class);
        DBColourFixture.check(colour, savedDBColour);
    }

    @Test
    public void saveEmpyFieldOverExisting() throws IOException {
        DBColour colour = DBColourFixture.create("1L");
        colourDAO.save(colour);
        GetResponse response = elasticsearchClient.getClient().prepareGet().setId("1L").setIndex(elasticsearchClient.getIndexName())
                .setType(ColourDAO.COLOUR_TYPE).get();
        DBColour savedDBColour = new ObjectMapper().readValue(response.getSourceAsString(), DBColour.class);
        savedDBColour.setName(null);
        colourDAO.save(savedDBColour);
        ;
        GetResponse response1 = elasticsearchClient.getClient().prepareGet().setId("1L").setIndex(elasticsearchClient.getIndexName())
                .setType(ColourDAO.COLOUR_TYPE).get();
        DBColour retrievedColour = new ObjectMapper().readValue(response1.getSourceAsString(), DBColour.class);
        assertNull(retrievedColour.getName());
    }

    @Test
    public void getByIdTest() throws InterruptedException {
        String id = UUID.randomUUID().toString();
        DBColour colour = DBColourFixture.create(id);
        colourDAO.save(colour);
        Thread.sleep(1000);
        DBColour gettedColour = colourDAO.getById(id);
        DBColourFixture.check(colour, gettedColour);
    }

    @Test
    public void getByNameTest() throws InterruptedException {
        String name = "blue";
        DBColour colour = DBColourFixture.create("10L");
        colour.setName(name);
        colourDAO.save(colour);
        Thread.sleep(1000);
        DBColour gettedColour = colourDAO.getByName(name);
        DBColourFixture.check(colour, gettedColour);
    }

    @Test
    public void getAllColoursTest() throws InterruptedException {
        DBColour colour1 = DBColourFixture.create("10L");
        colour1.setName("black");
        DBColour colour2 = DBColourFixture.create("20L");
        colour2.setName("white");
        DBColour colour3 = DBColourFixture.create("30L");
        colour3.setName("gray");
        colourDAO.save(colour1);
        Thread.sleep(1000);
        colourDAO.save(colour2);
        Thread.sleep(1000);
        colourDAO.save(colour3);
        Thread.sleep(1000);

        List<DBColour> gettedColours = colourDAO.getAll();
        assertEquals(gettedColours.size(), 3);
        // for (int i = 0; i < gettedColours.size(); i++) {
        // DBColourFixture.check(colourList.get(i), gettedColours.get(i));
        // }
    }
}
