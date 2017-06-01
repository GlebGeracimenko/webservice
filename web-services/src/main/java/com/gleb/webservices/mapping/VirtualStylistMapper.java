package com.gleb.webservices.mapping;

import com.gleb.dao.objects.DBVirtualStylistCanvas;
import com.gleb.webservices.bo.BOVirtualStylistCanvas;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by gleb on 03.11.15.
 */
@Component
public class VirtualStylistMapper {

    private Mapper mapper;

    @PostConstruct
    public void init() {
        mapper = new DozerBeanMapper();
    }

    public DBVirtualStylistCanvas map(BOVirtualStylistCanvas boVirtualStylistCanvas) {
        if (boVirtualStylistCanvas == null) {
            return null;
        }
        return mapper.map(boVirtualStylistCanvas, DBVirtualStylistCanvas.class);
    }

    public BOVirtualStylistCanvas map(DBVirtualStylistCanvas dbVirtualStylistCanvas) {
        if (dbVirtualStylistCanvas == null) {
            return null;
        }
        return mapper.map(dbVirtualStylistCanvas, BOVirtualStylistCanvas.class);
    }

}
