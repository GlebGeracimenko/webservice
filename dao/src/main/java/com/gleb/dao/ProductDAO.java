package com.gleb.dao;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gleb.dao.objects.DBProduct;
import com.gleb.dao.objects.DBProductListResponse;
import com.gleb.dao.utils.SearchQueryBuilder;

public interface ProductDAO {
    public static final String PRODUCT_TYPE = "products";
    public static final String BRANDS_AGGREGATION_NAME = "brandsAggs";

    public DBProduct getById(String id) throws JsonParseException, JsonMappingException, IOException;
    
    public void deleteProduct(String id) throws JsonParseException, JsonMappingException, IOException;

    public DBProduct getByImportedId(String importedId);

    public List<DBProduct> findByFilter(Map<String, Object> filters);

    public List<DBProduct> findForGender(int gender, int count);

    public List<DBProduct> getBySkus(List<String> skus);

    public Set<DBProduct> getByIds(Collection<String> ids) throws JsonParseException, JsonMappingException, IOException;

    public String saveProduct(DBProduct product) throws JsonProcessingException;

    public DBProductListResponse searchProducts(SearchQueryBuilder builder, int from, int size) throws JsonParseException, JsonMappingException, IOException;
}