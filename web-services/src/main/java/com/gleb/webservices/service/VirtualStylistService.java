package com.gleb.webservices.service;

import java.util.UUID;

import com.gleb.webservices.bo.BOVirtualStylistCanvas;

/**
 * Created by gleb on 03.11.15.
 */
public interface VirtualStylistService {

    BOVirtualStylistCanvas getById(UUID id);

    boolean delete(UUID id);

    UUID save(BOVirtualStylistCanvas stylistCanvas);

}
