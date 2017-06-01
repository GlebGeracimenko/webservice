package com.gleb.dao.fixture;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.gleb.dao.objects.DBColourCombination;

public class DBColourCombinationsFixture {

    public static DBColourCombination create(String id, String combinationName) {
        DBColourCombination dbColourComb = new DBColourCombination();
        dbColourComb.setId(id);
        dbColourComb.setMainColourName("orange");
        dbColourComb.setSecondColourName("blue");
        dbColourComb.setCombinationName(combinationName);
        List<String> colours = new ArrayList<>();
        colours.add("1L");
        colours.add("3L");
        colours.add("27L");
        dbColourComb.setCombinedColourIds(colours);

        return dbColourComb;
    }

    public static void check(DBColourCombination actual, DBColourCombination expected) {
        assertThat(actual.getId(), equalTo(expected.getId()));
        assertThat(actual.getMainColourName(), equalTo(expected.getMainColourName()));
        assertThat(actual.getSecondColourName(), equalTo(expected.getSecondColourName()));
        assertThat(actual.getCombinationName(), equalTo(expected.getCombinationName()));
        assertThat(actual.getCombinedColourIds(), equalTo(expected.getCombinedColourIds()));
    }
}