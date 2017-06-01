package com.gleb.dao.fixture;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.gleb.dao.objects.DBColour;
import com.gleb.dao.objects.Colour;

public class DBColourFixture {

    public static DBColour create(String id) {
        DBColour dbColour = new DBColour();
        dbColour.setId(id);
        dbColour.setName("orange");
        List<String> colours = new ArrayList<>();
        colours.add("pink");
        colours.add("red");
        colours.add("yellow");
        dbColour.setColours(colours);
        Colour exampleColour = new Colour();
        exampleColour.setRed(334);
        exampleColour.setGreen(233);
        exampleColour.setBlue(125);
        dbColour.setExampleColour(exampleColour);
        dbColour.setGroup("red");
        return dbColour;
    }

    public static void check(DBColour actual, DBColour expected) {
        assertThat(actual.getId(), equalTo(expected.getId()));
        assertThat(actual.getName(), equalTo(expected.getName()));
        assertThat(actual.getGroup(), equalTo(expected.getGroup()));
        assertThat(actual.getExampleColour(), equalTo(expected.getExampleColour()));
        assertTrue(actual.getColours().containsAll(expected.getColours()));
    }
}