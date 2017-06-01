package com.gleb.dao;

import java.util.List;
import java.util.UUID;

import com.gleb.dao.objects.DBVirtualStylistCanvas;

/**
 * Created by gleb on 31.10.15.
 */
public interface VirtualStylistCanvasDAO {

    UUID save(DBVirtualStylistCanvas virtualStylistCanvas);

    boolean update(DBVirtualStylistCanvas virtualStylistCanvas);

    boolean delete(UUID id);

    DBVirtualStylistCanvas getById(UUID id);

    List<DBVirtualStylistCanvas> getByUserId(UUID userId);

    DBVirtualStylistCanvas getByIdAndUserId(UUID id, UUID userId);

}
