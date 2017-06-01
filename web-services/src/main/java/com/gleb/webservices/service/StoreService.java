package com.gleb.webservices.service;

import com.gleb.webservices.bo.BOStore;

public interface StoreService {
    
    String saveStore(BOStore store);

    BOStore getStoreById(String id);
    
    BOStore getStoresByName(String nameStore);
    
}
