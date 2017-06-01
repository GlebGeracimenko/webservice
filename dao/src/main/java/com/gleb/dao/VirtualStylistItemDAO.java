package com.gleb.dao;

import java.util.UUID;

import com.gleb.dao.objects.DBVirtualStylistCanvasItem;

/**
 * Created by gleb on 31.10.15.
 */
public interface VirtualStylistItemDAO {

    UUID save(DBVirtualStylistCanvasItem stylistItem);

    boolean delete(UUID id);

    DBVirtualStylistCanvasItem getById(UUID id);

    DBVirtualStylistCanvasItem getBySKU(String sku);

}
