package com.gleb.webservices.service;

import java.util.*;

import com.gleb.webservices.bo.BOProductListResponse;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOProduct;
import com.gleb.webservices.rest.Filters;

public interface ProductService {

    BOProduct getProduct(String id);

    void deleteProduct(String id);
    
    BOProductListResponse getProducts(Collection<String> ids, UUID userId);

    Set<BOProduct> getProductsByTopCategory(String topCategory, String searchText, DBUser dbUser, int from);

    Set<BOProduct> getProductsByTopCategoryAndColours(String topCategory, List<String> colours, DBUser dbUser, int from);

    Set<BOProduct> getProductsByPriceRecommendation(String topCategory, ArrayList<Integer> priceClusters, int from);

    Set<BOProduct> getProductsByTopCategoryStyleAndPrice(String topCategory, String style, ArrayList<Integer> priceClusters, int from);

    BOProductListResponse getRecommendations(DBUser dbUser);

    String saveProduct(BOProduct product);

    /**
     * The difference between searchProducts and getProducts is that searh is
     * searching by "contains" some values in field. Instead of "full match"
     * 
     * @param filters
     * @param dbUser
     * @param from
     * @return
     */
    BOProductListResponse searchProducts(String searchText, DBUser dbUser, int from);

    BOProductListResponse searchProducts(String searchText, DBUser dbUser, int from, int limit);

    BOProductListResponse searchProductsByFilter(Filters filters, DBUser dbUser, int from);
     
}