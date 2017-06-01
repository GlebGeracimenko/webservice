package com.gleb.webservices.mapping;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.gleb.webservices.bo.BOColour;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBColour;

/**
 * Created by gleb on 16.09.15.
 */
@Component
public class ColourMapper {

    private Mapper mapper;

    @PostConstruct
    public void init() {
        mapper = new DozerBeanMapper();
    }

    public BOColour map(DBColour dbColor) {
        if (dbColor == null) {
            return null;
        }
        return mapper.map(dbColor, BOColour.class);
    }

    public Set<BOColour> map(Collection<DBColour> dbColors) {
        if (dbColors == null) {
            return new HashSet<BOColour>();
        }
        Set<BOColour> result = new HashSet<BOColour>(dbColors.size());
        for (DBColour dbColor : dbColors) {
            BOColour product = mapper.map(dbColor, BOColour.class);
            result.add(product);
        }
        return result;
    }

    public DBColour map(BOColour boColor) {
        if (boColor == null)
            return null;
        return mapper.map(boColor, DBColour.class);
    }

}
