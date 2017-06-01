package com.gleb.webservices.mapping;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gleb.dao.objects.Colour;
import com.gleb.dao.objects.DBColour;
import com.gleb.webservices.bo.BOColour;

public class ColorMapperTest {

    @Test
    public void testFromElasticToBO_normalCase() {
        DBColour dbColor = new DBColour();
        Colour color = new Colour();
        color.setRed(334);
        color.setGreen(233);
        color.setBlue(125);
        List<String> colors = new ArrayList<>();
        colors.add("orange");
        colors.add("yellow");
        colors.add("lightorangeyellow");
        dbColor.setId("23L");
        dbColor.setExampleColour(color);
        dbColor.setColours(colors);

        ColourMapper mapper = new ColourMapper();
        mapper.init();

        BOColour mappedColour = mapper.map(dbColor);

        assertThat(mappedColour.getExampleColour(), equalTo(dbColor.getExampleColour())); // /???
        assertThat(mappedColour.getColours(), equalTo(dbColor.getColours()));
        assertThat(mappedColour.getId(), equalTo(dbColor.getId()));
        assertTrue(mappedColour.getColours().containsAll(dbColor.getColours()));
        assertTrue(dbColor.getColours().containsAll(mappedColour.getColours()));
    }

    @Test
    public void testFromBOtoElastic_normalCase() {
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

        ColourMapper mapper = new ColourMapper();
        mapper.init();

        DBColour mappedColour = mapper.map(boColour);

        assert (mappedColour.getExampleColour().equals(boColour.getExampleColour()));
        assertThat(mappedColour.getColours(), equalTo(boColour.getColours()));
        assertThat(mappedColour.getId(), equalTo(boColour.getId()));
        assertTrue(mappedColour.getColours().containsAll(boColour.getColours()));
        assertTrue(boColour.getColours().containsAll(mappedColour.getColours()));

    }
}