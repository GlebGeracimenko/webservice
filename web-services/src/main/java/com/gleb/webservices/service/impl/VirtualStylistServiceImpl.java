package com.gleb.webservices.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gleb.dao.cassandra.CQLVirtualStylistCanvasDAOImpl;
import com.gleb.dao.objects.DBVirtualStylistCanvas;
import com.gleb.webservices.bo.BOVirtualStylistCanvas;
import com.gleb.webservices.mapping.VirtualStylistMapper;
import com.gleb.webservices.service.IdsService;
import com.gleb.webservices.service.VirtualStylistService;

/**
 * Created by gleb on 03.11.15.
 */
@Service
public class VirtualStylistServiceImpl implements VirtualStylistService {

    @Autowired
    private VirtualStylistMapper virtualStylistMapper;

    @Autowired
    private IdsService idsService;

    @Autowired
    private CQLVirtualStylistCanvasDAOImpl canvasDAO;

    @Override
    public BOVirtualStylistCanvas getById(UUID id) {
        DBVirtualStylistCanvas canvas = canvasDAO.getById(id);
        return virtualStylistMapper.map(canvas);
    }

    @Override
    public boolean delete(UUID id) {
        return canvasDAO.delete(id);
    }

    @Override
    public UUID save(BOVirtualStylistCanvas stylistCanvas) {
        DBVirtualStylistCanvas canvas = virtualStylistMapper.map(stylistCanvas);
        if (canvas.getId() == null) {
            canvas.setId(idsService.getNewId());
        }
        return canvasDAO.save(canvas);
    }

}
