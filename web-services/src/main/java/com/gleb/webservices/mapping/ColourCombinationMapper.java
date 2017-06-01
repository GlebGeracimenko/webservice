package com.gleb.webservices.mapping;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.gleb.webservices.bo.BOColourCombinationInfo;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBColourCombination;
import com.gleb.webservices.bo.BOColourCombination;
import com.gleb.webservices.service.ColourService;

/**
 * @author Viacheslav Vasianovych
 */
@Component
public class ColourCombinationMapper {

    @Autowired
    private ColourService colourService;

    private Mapper mapper;

    @PostConstruct
    public void init() {
        mapper = new DozerBeanMapper();
    }

    public BOColourCombination map(DBColourCombination dbColourCombination) {
        if (dbColourCombination == null) {
            return null;
        }
        BOColourCombination boColourCombination = new BOColourCombination();
        boColourCombination.setId(dbColourCombination.getId());
        boColourCombination.setMainColourName(dbColourCombination.getMainColourName());
        boColourCombination.setSecondColourName(dbColourCombination.getSecondColourName());
        boColourCombination.setCombinationName(dbColourCombination.getCombinationName());
        List<String> bdCombinedColourIds = dbColourCombination.getCombinedColourIds();
        List<String> boCombinedColoursIds = boColourCombination.getCombinedColourIds();
        for (String id : bdCombinedColourIds) {
            boCombinedColoursIds.add(id);
        }
        boColourCombination.setCombinedColourIds(boCombinedColoursIds);

        return boColourCombination;
    }

    public Set<BOColourCombination> map(Collection<DBColourCombination> dbColorCombinations) {
        if (dbColorCombinations == null) {
            return new HashSet<BOColourCombination>();
        }
        Set<BOColourCombination> result = new HashSet<BOColourCombination>(dbColorCombinations.size());
        for (DBColourCombination dbColorCombination : dbColorCombinations) {
            BOColourCombination boColourCombination = map(dbColorCombination);
            if (boColourCombination != null) {
                result.add(boColourCombination);
            }
        }
        return result;
    }

    public DBColourCombination map(BOColourCombination boColourCombination) {
        if (boColourCombination == null) {
            return null;
        }
        DBColourCombination dbColourCombination = new DBColourCombination();
        dbColourCombination.setId(boColourCombination.getId());
        dbColourCombination.setMainColourName(boColourCombination.getMainColourName());
        dbColourCombination.setSecondColourName(boColourCombination.getSecondColourName());
        dbColourCombination.setCombinationName(boColourCombination.getCombinationName());
        List<String> boCombinedColoursIds = boColourCombination.getCombinedColourIds();
        List<String> dbCombinedColoursIds = dbColourCombination.getCombinedColourIds();
        for (String id : boCombinedColoursIds) {
            dbCombinedColoursIds.add(id);
        }
        dbColourCombination.setCombinedColourIds(dbCombinedColoursIds);

        return dbColourCombination;
    }

    public DBColourCombination map(BOColourCombinationInfo boColourCombinationInfo) {
        if (boColourCombinationInfo == null)
            return null;
        return mapper.map(boColourCombinationInfo, DBColourCombination.class);
    }
}