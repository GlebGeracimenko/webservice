package com.gleb.dao.objects;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents all necessary information about found products
 * 
 * @author Viacheslav Vasianovych
 *
 */
public class DBProductListResponse {
    private final Set<DBProduct> products = new HashSet<DBProduct>();
    private final Set<String> brandIds = new HashSet<String>();

    public Set<DBProduct> getProducts() {
        return products;
    }

    public void addProduct(DBProduct product) {
        products.add(product);
    }

    public Set<String> getBrandIds() {
        return brandIds;
    }

    public void addBrandId(String brandId) {
        brandIds.add(brandId);
    }
}
