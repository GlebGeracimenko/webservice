package com.gleb.webservices.service.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.gleb.webservices.bo.BOColour;
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

import com.gleb.dao.ColourDAO;
import com.gleb.dao.cassandra.BasicCQLCassandraDAO;
import com.gleb.dao.elasticsearch.ElasticsearchClient;
import com.gleb.dao.objects.Colour;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ColourServiceImplTest extends BasicIntegrationTest {

    @Autowired
    private ColourService serviceImpl;

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
    public void testSaveColour() throws Exception {
        BOColour boColour = new BOColour();
        Colour color = new Colour();
        color.setRed(334);
        color.setGreen(233);
        color.setBlue(125);
        List<String> colors = new ArrayList<>();
        colors.add("orange");
        colors.add("yellow");
        colors.add("lightorangeyellow");
        boColour.setId("23L");
        boColour.setExampleColour(color);
        boColour.setColours(colors);

        List<String> colourExpected = boColour.getColours();

        serviceImpl.saveColour(boColour);
        serviceImpl.saveColour(boColour);
        serviceImpl.saveColour(boColour);

        // Tricky hack
        Thread.sleep(2000);

        // BOColour gettedColour1 = serviceImpl.getColourByName("lightorangeyellow");
        // assertThat(colors, equalTo(gettedColour1.getColours()));

        BOColour gettedColour = serviceImpl.getColourById("23L");
        List<String> colourActual = gettedColour.getColours();
        assertThat(colourExpected, equalTo(colourActual));
        assertThat(color.getBlue(), equalTo(gettedColour.getExampleColour().getBlue()));
    }
}