package com.gleb.webservices.mapping;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.gleb.webservices.bo.BOStyle;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBStyle;

/**
 * Mappers provide ability to map entities from DB layer (elastic, mongo or cassandra) to Business layer (for services or REST) to have control of privacy
 * 
 * @author Viacheslav Vasianovych
 *
 */
@Component
public class StylesMapper {

    private Mapper mapper;

    @PostConstruct
    public void init() {
        mapper = new DozerBeanMapper();
    }

    public BOStyle map(DBStyle dbStyle) {
        if (dbStyle == null) {
            return null;
        }
        return mapper.map(dbStyle, BOStyle.class);
    }

    public Set<BOStyle> map(Collection<DBStyle> dbStyles) {
        if (dbStyles == null) {
            return new HashSet<BOStyle>();
        }
        Set<BOStyle> result = new HashSet<BOStyle>(dbStyles.size());
        for (DBStyle dbStyle : dbStyles) {
        	BOStyle style = mapper.map(dbStyle, BOStyle.class);
            result.add(style);
        }
        return result;
    }

    public DBStyle map(BOStyle boStyle) {
        if (boStyle == null)
            return null;
        return mapper.map(boStyle, DBStyle.class);
    }
}
