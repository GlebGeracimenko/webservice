package com.gleb.webservices.service;

import java.util.Collection;
import java.util.Set;

import com.gleb.webservices.bo.BOColour;
import com.gleb.webservices.bo.BOColourCombination;

/**
 * Created by gleb on 16.09.15.
 */
public interface ColourService {

    String saveColour(BOColour color);

    BOColour getColourById(String id);

    Set<BOColour> getColours();

    BOColour getColourByName(String nameColor);

    Collection<BOColourCombination> getAllColourCombinations();

    Collection<BOColourCombination> getColourCombinationsByColourName(String colourName);

    BOColourCombination getColourCombinationsByColourNameAndType(String colourName, String combinationName);

    void saveColourCombinations(BOColourCombination boColourCombination);

}