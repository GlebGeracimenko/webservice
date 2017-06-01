package com.gleb.webservices.mapping;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBStore;
import com.gleb.webservices.bo.BOStore;

/**
 * Mappers provide ability to map entities from DB layer (elastic, mongo or
 * cassandra) to Business layer (for services or REST) to have control of
 * privacy
 * 
 * @author Viacheslav Vasianovych
 *
 */
@Component
public class StoresMapper {

    private Mapper mapper;

    @PostConstruct
    public void init() {
        mapper = new DozerBeanMapper();
    }

    public BOStore map(DBStore dbStore) {
        if (dbStore == null) {
            return null;
        }
        return mapper.map(dbStore, BOStore.class);
    }

    public Set<BOStore> map(Collection<DBStore> dbStores) {
        if (dbStores == null) {
            return new HashSet<BOStore>();
        }
        Set<BOStore> result = new HashSet<BOStore>(dbStores.size());
        for (DBStore dbStore : dbStores) {
            BOStore product = mapper.map(dbStore, BOStore.class);
            result.add(product);
        }
        return result;
    }

    public DBStore map(BOStore boStore) {
        if (boStore == null)
            return null;
        return mapper.map(boStore, DBStore.class);
    }
}
