package com.gleb.webservices.mapping;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBBrand;
import com.gleb.webservices.bo.BOBrand;
import com.gleb.webservices.bo.BOBrandWithFollowing;

/**
 * Mappers provide ability to map entities from DB layer (elastic, mongo or cassandra) to Business layer (for services or REST) to have control of privacy
 * 
 * @author Viacheslav Vasianovych
 *
 */
@Component
public class BrandsMapper {

    private Mapper mapper;

    @PostConstruct
    public void init() {
        mapper = new DozerBeanMapper();
    }

    public BOBrand map(DBBrand dbBrand) {
        if (dbBrand == null) {
            return null;
        }
        return mapper.map(dbBrand, BOBrand.class);
    }
    
    public BOBrandWithFollowing mapWithFollowing(BOBrand boBrand) {
        if (boBrand == null) {
            return null;
        }
        return mapper.map(boBrand, BOBrandWithFollowing.class);
    }

    public Set<BOBrand> map(Collection<DBBrand> dbBrands) {
        if (dbBrands == null) {
            return new HashSet<BOBrand>();
        }
        Set<BOBrand> result = new HashSet<BOBrand>(dbBrands.size());
        for (DBBrand dbProduct : dbBrands) {
            BOBrand product = mapper.map(dbProduct, BOBrand.class);
            result.add(product);
        }
        return result;
    }

    public DBBrand map(BOBrand boBrand) {
        if (boBrand == null)
            return null;
        return mapper.map(boBrand, DBBrand.class);
    }
}
