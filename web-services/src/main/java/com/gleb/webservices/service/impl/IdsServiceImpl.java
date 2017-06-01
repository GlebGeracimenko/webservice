package com.gleb.webservices.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gleb.webservices.service.IdsService;

@Service
public class IdsServiceImpl implements IdsService {

    @Override
    public UUID getNewId() {
        return UUID.randomUUID();
    }

    @Override
    public String getNewIdAsString() {
        return UUID.randomUUID().toString();
    }

    @Override
    public UUID convert(String id) {
        return UUID.fromString(id);
    }

    @Override
    public List<UUID> convert(List<String> ids) {
        List<UUID> uuids = new ArrayList<UUID>();
        for(String id : ids) {
            uuids.add(UUID.fromString(id));
        }
        return uuids;
    }
}
