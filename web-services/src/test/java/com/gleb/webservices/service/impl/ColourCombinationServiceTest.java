package com.gleb.webservices.service.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gleb.webservices.bo.BOColour;
import com.gleb.webservices.mapping.ColourCombinationMapper;
import com.gleb.webservices.service.ColourService;
import com.gleb.webservices.tests.BasicIntegrationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gleb.dao.ColourCombinationsDAO;
import com.gleb.dao.ColourDAO;
import com.gleb.dao.cassandra.BasicCQLCassandraDAO;
import com.gleb.dao.elasticsearch.ElasticsearchClient;
import com.gleb.dao.objects.Colour;
import com.gleb.webservices.bo.BOColourCombination;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ColourCombinationServiceTest extends BasicIntegrationTest {

    @Autowired
    private ColourService serviceImpl;

    @Autowired
    private ColourCombinationMapper colourCombinationMapper;

    @Autowired
    private ColourCombinationsDAO colourCombinationDAO;

    @Autowired
    private ColourDAO colourDAO;

    @Autowired
    private ElasticsearchClient clientHolder;

    @Autowired
    private BasicCQLCassandraDAO basicCassandraDAO;

    @Autowired
    private ElasticsearchClient client;

    public void cleanUp() {
	truncateElasticsearchType(ColourDAO.COLOUR_TYPE);
	truncateElasticsearchType(ColourCombinationsDAO.COLOUR_COMBINATIONS_TYPE);
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
    public void testGetBOColourComb() throws Exception {
	BOColourCombination boColourCombination = new BOColourCombination();
	boColourCombination.setId("89");
	boColourCombination.setMainColourName("yellow"); // group name for the BOColour #1
	boColourCombination.setSecondColourName("greenyellowbrown"); // group name for the BOColour #2
	boColourCombination.setCombinationName("triadic");
	List<String> list = new ArrayList<>();
	list.add("1L");
	list.add("20L");
	list.add("17L");
	boColourCombination.setCombinedColourIds(list);

	serviceImpl.saveColourCombinations(boColourCombination);

	BOColour boColourMain = new BOColour();
	Colour colour = new Colour();
	colour.setBlue(233);
	colour.setGreen(88);
	colour.setRed(118);
	boColourMain.setExampleColour(colour);
	boColourMain.setGroup("yellow");
	boColourMain.setId("1888L");
	boColourMain.setName("light_yellow");
	List<String> list1 = new ArrayList<>();
	list1.add("yewllow1");
	list1.add("yellow2");
	boColourMain.setColours(list1);

	serviceImpl.saveColour(boColourMain);

	BOColour boColourSecond = new BOColour();
	Colour colour1 = new Colour();
	colour1.setBlue(233);
	colour1.setGreen(88);
	colour1.setRed(118);
	boColourSecond.setExampleColour(colour1);
	boColourSecond.setGroup("greenyellowbrown");
	boColourSecond.setId("888L");
	boColourSecond.setName("brown");
	List<String> list2 = new ArrayList<>();
	list1.add("brown1");
	list1.add("brown2");
	boColourSecond.setColours(list2);

	serviceImpl.saveColour(boColourSecond);
	String groupColourName = boColourMain.getGroup();
	Collection<BOColourCombination> colours = serviceImpl.getColourCombinationsByColourName(groupColourName);
	for (BOColourCombination col : colours) { // has 1 element only
	    assertThat(col.getMainColourName(), equalTo("yellow"));
	    assertThat(col.getCombinationName(), equalTo("triadic"));
	    assertThat(col.getSecondColourName(), equalTo("greenyellowbrown"));
	}
    }
}