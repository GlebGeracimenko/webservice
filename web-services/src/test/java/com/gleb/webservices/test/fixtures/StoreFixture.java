package com.gleb.webservices.test.fixtures;

import com.gleb.webservices.bo.BOStore;

/**
 * Created by gleb on 15.09.15.
 */
public class StoreFixture {
    public static BOStore createStore(Long id, Long networkId) {
        BOStore boStore = new BOStore();
        boStore.setId(id);
        boStore.setNetworkId(networkId);
        boStore.setName("FC");
        boStore.setAddress("testAddress");
        boStore.setLatitude("testLatitude");
        boStore.setLongitude("testLongitude");
        boStore.setDescription("TEST TEST DESCRIPTION TEST TEST");
        return boStore;
    }
}
