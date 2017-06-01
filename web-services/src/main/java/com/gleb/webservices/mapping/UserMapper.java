package com.gleb.webservices.mapping;

import java.util.UUID;

import javax.annotation.PostConstruct;

import com.gleb.webservices.bo.BOUser;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import com.gleb.dao.objects.DBUser;

/**
 * Mappers provide ability to map entities from DB layer (elastic, mongo or
 * cassandra) to Business layer (for services or REST) to have control of
 * privacy
 * 
 * @author Viacheslav Vasianovych
 *
 */
@Component
public class UserMapper {

    private Mapper mapper;

    @PostConstruct
    public void init() {
        mapper = new DozerBeanMapper();
    }

    public BOUser map(DBUser dbuser) {
        if (dbuser == null) {
            return null;
        }
        return mapper.map(dbuser, BOUser.class);
    }

    //TODO: dirty hack, should be fixed with dozer mapping strategy
    public DBUser map(DBUser dbuser, BOUser updatedUser) {
        if (dbuser == null) {
            return null;
        }
        UUID id = dbuser.getId();
        mapper.map(updatedUser, dbuser);
        dbuser.setId(id);
        return dbuser;
    }

    public DBUser map(BOUser boBrand) {
        if (boBrand == null)
            return null;
        return mapper.map(boBrand, DBUser.class);
    }
}
