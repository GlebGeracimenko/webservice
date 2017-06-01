package com.gleb.dao;

import java.util.List;

import com.gleb.dao.objects.DBColourCombination;

/**
 * Created by gleb on 16.09.15.
 */
public interface ColourCombinationsDAO {

    public static final String COLOUR_COMBINATIONS_TYPE = "colourCombinations";

    public void save(DBColourCombination dbColourCombination);

    public void delete(DBColourCombination dbColourCombination);

    public List<DBColourCombination> getAll();

    public List<DBColourCombination> getByColour(String colourName);

    public DBColourCombination getByColourAndType(String combinationName, String colourName);

}