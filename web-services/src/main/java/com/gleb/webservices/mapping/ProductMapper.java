package com.gleb.webservices.mapping;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.gleb.webservices.bo.BOProduct;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBProduct;

/**
 * Mappers provide ability to map entities from DB layer (elastic, mongo or cassandra) to Business layer (for services or REST) to have control of privacy
 * 
 * @author Viacheslav Vasianovych
 *
 */
@Component
public class ProductMapper {

    private Mapper mapper;

    @PostConstruct
    public void init() {
        mapper = new DozerBeanMapper();
    }

    public BOProduct map(DBProduct dbProduct) {
        if (dbProduct == null) {
            return null;
        }
        return mapper.map(dbProduct, BOProduct.class);
    }

    public Set<BOProduct> map(Collection<DBProduct> dbProducts) {
        if (dbProducts == null) {
            return new HashSet<BOProduct>();
        }
        Set<BOProduct> result = new HashSet<BOProduct>(dbProducts.size());
        for (DBProduct dbProduct : dbProducts) {
            BOProduct product = mapper.map(dbProduct, BOProduct.class);
            result.add(product);
        }
        return result;
    }

    public DBProduct map(BOProduct boProduct) {
        if (boProduct == null) {
            return null;
        }
        return mapper.map(boProduct, DBProduct.class);
    }
}
