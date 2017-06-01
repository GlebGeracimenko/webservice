package com.gleb.webservices.service.impl;

import com.gleb.webservices.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gleb.dao.StoreDAO;
import com.gleb.dao.objects.DBStore;
import com.gleb.webservices.bo.BOStore;
import com.gleb.webservices.mapping.StoresMapper;
import com.gleb.webservices.service.IdsService;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreDAO storeDAO;

    @Autowired
    private StoresMapper storesMapper;

    @Autowired
    private IdsService idsService;

    @Override
    public String saveStore(BOStore store) {
        DBStore dbStore = storesMapper.map(store);
        if (dbStore == null) {
            throw new RuntimeException("wrong store mapping");
        }
        if (dbStore.getId() == null) {
            dbStore.setId(idsService.getNewIdAsString());
        }
        String result = storeDAO.save(dbStore);
        return result;
    }

    @Override
    public BOStore getStoreById(String id) {
        BOStore boStore = storesMapper.map(storeDAO.getById(id));
        return boStore;
    }

    @Override
    public BOStore getStoresByName(String nameStore) {
        return null;
    }

}
