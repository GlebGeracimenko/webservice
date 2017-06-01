package com.gleb.webservices.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.gleb.webservices.bo.BOColour;
import com.gleb.webservices.mapping.ColourCombinationMapper;
import com.gleb.webservices.service.ColourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gleb.dao.ColourCombinationsDAO;
import com.gleb.dao.ColourDAO;
import com.gleb.dao.objects.DBColour;
import com.gleb.dao.objects.DBColourCombination;
import com.gleb.webservices.bo.BOColourCombination;
import com.gleb.webservices.mapping.ColourMapper;
import com.gleb.webservices.service.IdsService;

/**
 * Created by gleb on 16.09.15.
 */
@Service
public class ColourServiceImpl implements ColourService {

    @Autowired
    private ColourDAO colourDAO;

    @Autowired
    private ColourCombinationsDAO colourCombinationsDAO;

    @Autowired
    private ColourCombinationMapper colourCombinationMapper;

    @Autowired
    private ColourMapper colorMapper;

    @Autowired
    private IdsService idsService;

    @Override
    public String saveColour(BOColour color) {
        DBColour dbColor = colorMapper.map(color);
        if (dbColor == null) {
            throw new RuntimeException("wrong store mapping");
        }
        if (dbColor.getId() == null) {
            dbColor.setId(idsService.getNewIdAsString());
        }
        String result = colourDAO.save(dbColor);
        return result;
    }

    @Override
    public BOColour getColourById(String id) {
        return colorMapper.map(colourDAO.getById(id));
    }

    @Override
    public BOColour getColourByName(String nameColor) {
        return colorMapper.map(colourDAO.getByName(nameColor));
    }

    @Override
    public Set<BOColour> getColours() {
        return colorMapper.map(colourDAO.getAll());
    }

    @Override
    public Collection<BOColourCombination> getAllColourCombinations() {
        List<DBColourCombination> dbColourCombinations = colourCombinationsDAO.getAll();
        return colourCombinationMapper.map(dbColourCombinations);
    }

    @Override
    public Collection<BOColourCombination> getColourCombinationsByColourName(String colourName) {
        List<DBColourCombination> dbColourCombinations = colourCombinationsDAO.getByColour(colourName);
        return colourCombinationMapper.map(dbColourCombinations);
    }

    @Override
    public void saveColourCombinations(BOColourCombination boColourCombination) {
        DBColourCombination dbColorCombination = colourCombinationMapper.map(boColourCombination);
        if (dbColorCombination == null) {
            throw new RuntimeException("wrong store mapping");
        }
        colourCombinationsDAO.save(dbColorCombination);
    }

    @Override
    public BOColourCombination getColourCombinationsByColourNameAndType(String colourName, String combinationName) {
        DBColourCombination dbColourCombinations = colourCombinationsDAO.getByColourAndType(combinationName, colourName);
        return colourCombinationMapper.map(dbColourCombinations);
    }
}