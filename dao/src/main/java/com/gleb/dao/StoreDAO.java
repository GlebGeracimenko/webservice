package com.gleb.dao;

import com.gleb.dao.objects.DBStore;

/**
 * Created by gleb on 14.09.15.
 */
public interface StoreDAO {

    public String save(DBStore dbStore);

    public boolean update(DBStore dbStore);

    public DBStore getById(String id);

    public DBStore getByName(String nameStore);

    //???public boolean delete(DBStore dbStore);

}
