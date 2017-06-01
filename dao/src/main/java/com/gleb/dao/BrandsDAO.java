package com.gleb.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.gleb.dao.objects.DBBrand;
import com.gleb.dao.objects.DBUser;

/**
 * Interface for Brands access
 * 
 * @author Viacheslav Vasianovych
 *
 */
public interface BrandsDAO {

    String saveBrand(DBBrand dbBrand);
    
    Collection<DBBrand> getBrands(Collection<String> brandIds);

    DBBrand getBrand(String id);

    DBBrand getBrandByName(String name);

    DBBrand getBrandByDescription(String descPhrase);
    
    List<DBBrand> getAllBrands(Map<String, Object> query, DBUser dbUser, int from, int size);
    
    List<DBBrand> getAllBrands();
}
