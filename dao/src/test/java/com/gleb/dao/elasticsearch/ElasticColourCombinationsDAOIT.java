package com.gleb.dao.elasticsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gleb.dao.objects.DBColourCombination;
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
import com.gleb.dao.ColourCombinationsDAO;
import com.gleb.dao.fixture.DBColourCombinationsFixture;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ElasticColourCombinationsDAOIT extends ElasticAbstractIT {

    @Autowired
    private ColourCombinationsDAO colourCombDAO;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Before
    public void before() {
        truncateType(ElasticColourCombinationDAOImpl.COLOUR_COMBINATIONS_TYPE);
    }

    @After
    public void after() {
        truncateType(ElasticColourCombinationDAOImpl.COLOUR_COMBINATIONS_TYPE);
    }

    @Test
    public void save_normalCase() throws IOException {
        DBColourCombination colourComb = DBColourCombinationsFixture.create("1", "triadic");
        colourCombDAO.save(colourComb);
        GetResponse response = elasticsearchClient.getClient().prepareGet().setId("1").setIndex(elasticsearchClient.getIndexName())
                .setType(ColourCombinationsDAO.COLOUR_COMBINATIONS_TYPE).get();
        DBColourCombination savedDBColourComb = new ObjectMapper().readValue(response.getSourceAsString(), DBColourCombination.class);
        DBColourCombinationsFixture.check(colourComb, savedDBColourComb);
    }

    @Test
    public void delete_normalCase() throws IOException, InterruptedException {
        DBColourCombination colourComb = DBColourCombinationsFixture.create("1", "triadic");
        colourCombDAO.save(colourComb);
        Thread.sleep(1000);
        colourCombDAO.delete(colourComb);
        Thread.sleep(1000);
        GetResponse response = elasticsearchClient.getClient().prepareGet().setId("1").setIndex(elasticsearchClient.getIndexName())
                .setType(ColourCombinationsDAO.COLOUR_COMBINATIONS_TYPE).get();
        assertNull(response.getSource());
    }

    @Test
    public void getByColourTest() throws InterruptedException {
        DBColourCombination colourComb = DBColourCombinationsFixture.create("1", "triadic");
        colourCombDAO.save(colourComb);
        Thread.sleep(1000);
        List<DBColourCombination> gettedColourComb = colourCombDAO.getByColour(colourComb.getMainColourName());
        Thread.sleep(1000);
        DBColourCombinationsFixture.check(colourComb, gettedColourComb.get(0));
    }

    @Test
    public void getAllCombinations() throws InterruptedException {
        List<DBColourCombination> allColoursCombinations = new ArrayList<>();
        DBColourCombination colourCombination = DBColourCombinationsFixture.create("1", "triadic");
        allColoursCombinations.add(colourCombination);
        colourCombDAO.save(colourCombination);
        colourCombination = DBColourCombinationsFixture.create("10", "monochromatic");
        allColoursCombinations.add(colourCombination);
        colourCombDAO.save(colourCombination);
        colourCombination = DBColourCombinationsFixture.create("5", "complementary");
        allColoursCombinations.add(colourCombination);
        colourCombDAO.save(colourCombination);
        colourCombination = DBColourCombinationsFixture.create("7", "triadic");
        allColoursCombinations.add(colourCombination);
        colourCombDAO.save(colourCombination);
        Thread.sleep(1000);

        List<DBColourCombination> allSavedCombinations = colourCombDAO.getAll();

        assertEquals(allSavedCombinations.size(), 4);
    }

    @Test
    public void getByColourAndTypeTest() throws InterruptedException {
        DBColourCombination colourCombination = DBColourCombinationsFixture.create("1", "complementary");
        colourCombDAO.save(colourCombination);
        Thread.sleep(1000);
        DBColourCombination savedCombination = colourCombDAO.getByColourAndType("complementary", colourCombination.getMainColourName());
        DBColourCombinationsFixture.check(colourCombination, savedCombination);
    }
}