package com.gleb.dao.elasticsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.elasticsearch.action.get.GetResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gleb.dao.ProductDAO;
import com.gleb.dao.fixture.DBProductFixture;
import com.gleb.dao.objects.DBProduct;
import com.gleb.dao.objects.DBProductListResponse;
import com.gleb.dao.utils.SearchQueryBuilder;

/**
 * @author Viacheslav Vasianovych
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ElasticProductsDAOIT extends ElasticAbstractIT {

    @Autowired
    private ElasticProductDAOImpl productsDAO;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Before
    public void before() throws IOException {
        truncateType(ProductDAO.PRODUCT_TYPE);
    }

    @After
    public void after() {
        truncateType(ProductDAO.PRODUCT_TYPE);
    }

    @Test
    public void save_normalCase() throws IOException {
        DBProduct product = DBProductFixture.create(UUID.randomUUID().toString());
        productsDAO.saveProduct(product);
        GetResponse response = elasticsearchClient.getClient().prepareGet().setId(product.getId()).setIndex(elasticsearchClient.getIndexName())
                .setType(ElasticProductDAOImpl.PRODUCT_TYPE).get();
        DBProduct savedDBProduct = new ObjectMapper().readValue(response.getSourceAsString(), DBProduct.class);
        DBProductFixture.check(savedDBProduct, product);
    }

    @Test
    public void saveEmpyFieldOverExisting() throws IOException {
        DBProduct product = DBProductFixture.create(UUID.randomUUID().toString());
        productsDAO.saveProduct(product);
        GetResponse response = elasticsearchClient.getClient().prepareGet().setId(product.getId()).setIndex(elasticsearchClient.getIndexName())
                .setType(ElasticProductDAOImpl.PRODUCT_TYPE).get();
        DBProduct savedDBProduct = new ObjectMapper().readValue(response.getSourceAsString(), DBProduct.class);
        savedDBProduct.setCategory(null);
        productsDAO.saveProduct(savedDBProduct);
        GetResponse response1 = elasticsearchClient.getClient().prepareGet().setId(product.getId()).setIndex(elasticsearchClient.getIndexName())
                .setType(ElasticProductDAOImpl.PRODUCT_TYPE).get();
        DBProduct retrievedProduct = new ObjectMapper().readValue(response1.getSourceAsString(), DBProduct.class);
        assertNull(retrievedProduct.getCategory());
    }

    @Test
    public void saveProductWithoutId() throws IOException {
        boolean thrown = false;
        DBProduct dbProduct = DBProductFixture.create(null);
        try {
            productsDAO.saveProduct(dbProduct);
        } catch (RuntimeException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void getProductById() throws IOException, InterruptedException {
        String id = "3L";
        DBProduct product = DBProductFixture.create(id);
        productsDAO.saveProduct(product);
        Thread.sleep(1000);
        DBProduct savedDBProduct = productsDAO.getById(id);
        DBProductFixture.check(product, savedDBProduct);
    }

    @Test
    public void getProductsByIds() throws IOException {
        DBProduct product = null;
        Collection<String> ids = new ArrayList<>();
        Set<DBProduct> products = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            product = DBProductFixture.create(UUID.randomUUID().toString());
            productsDAO.saveProduct(product);
            products.add(product);
            ids.add(product.getId());
        }

        Set<DBProduct> savedDBProducts = productsDAO.getByIds(ids);
        assertEquals(products, savedDBProducts);
    }

    @Test
    public void getProductsByImportedId() throws IOException, InterruptedException {
        String id = UUID.randomUUID().toString();
        DBProduct product = DBProductFixture.create(id);
        String importedId = product.getImportedId();
        productsDAO.saveProduct(product);
        Thread.sleep(1000);
        DBProduct savedDBProduct = productsDAO.getByImportedId(importedId);
        DBProductFixture.check(product, savedDBProduct);
    }

    @Test
    public void searchProductsByIdTest() throws InterruptedException, JsonParseException, JsonMappingException, IOException {
        DBProduct product = DBProductFixture.create(UUID.randomUUID().toString());
        productsDAO.saveProduct(product);
        Thread.sleep(1000);

        SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
        Collection<String> productIds = new ArrayList<>();
        productIds.add(product.getId());
        queryBuilder.addProductIds(productIds);
        DBProductListResponse products = productsDAO.searchProducts(queryBuilder, 0, 10);

        DBProduct savedDBProduct = getProductFromSet(products);
        DBProductFixture.check(savedDBProduct, product);
    }

    @Test
    public void searchProductsWithBrandTest() throws InterruptedException, JsonParseException, JsonMappingException, IOException {
        DBProduct product = DBProductFixture.create(UUID.randomUUID().toString());
        product.setBrand("3");
        productsDAO.saveProduct(product);
        Thread.sleep(1000);

        SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
        Collection<String> brandIds = new ArrayList<>();
        brandIds.add("3");
        queryBuilder.addBrandIds(brandIds);
        DBProductListResponse products = productsDAO.searchProducts(queryBuilder, 0, 10);

        DBProduct savedDBProduct = getProductFromSet(products);
        DBProductFixture.check(product, savedDBProduct);
    }

    @Test
    public void searchProductsWithColoursListTest() throws IOException, InterruptedException {
        DBProduct product = DBProductFixture.create(UUID.randomUUID().toString());
        List<String> colour = new ArrayList<>();
        colour.add("verylightgrey");
        colour.add("wite");
        product.setColor(colour);
        productsDAO.saveProduct(product);
        Thread.sleep(1000);

        SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
        Collection<String> colours = new ArrayList<>();
        colours.add("verylightgrey");
        colours.add("wite");
        queryBuilder.addColours(colours);

        DBProductListResponse products = productsDAO.searchProducts(queryBuilder, 0, 10);

        DBProduct savedDBProduct = getProductFromSet(products);
        DBProductFixture.check(savedDBProduct, product);
    }

    @Test
    public void searchProductsWithDifferentColoursListTest() throws IOException, InterruptedException {
        DBProduct product1 = DBProductFixture.create(UUID.randomUUID().toString());
        List<String> colour1 = new ArrayList<>();
        colour1.add("verylightgrey");
        product1.setColor(colour1);
        DBProduct product2 = DBProductFixture.create(UUID.randomUUID().toString());
        List<String> colour2 = new ArrayList<>();
        colour2.add("wite");
        product2.setColor(colour2);

        productsDAO.saveProduct(product1);
        productsDAO.saveProduct(product2);
        Thread.sleep(1000);

        SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
        Collection<String> colours = new ArrayList<>();
        colours.add("verylightgrey");
        colours.add("wite");
        queryBuilder.addColours(colours);

        DBProductListResponse productsList = productsDAO.searchProducts(queryBuilder, 0, 10);

        assertEquals(productsList.getProducts().size(), 2);
    }

    @Test
    public void searchProductsWithExcludedProduct() throws InterruptedException, JsonParseException, JsonMappingException, IOException {
        DBProduct product = DBProductFixture.create(UUID.randomUUID().toString());
        productsDAO.saveProduct(product);
        Thread.sleep(1000);

        SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
        Collection<String> productIdsToExclude = new ArrayList<>();
        productIdsToExclude.add(product.getId());
        queryBuilder.addExcludedProductIds(productIdsToExclude);
        DBProductListResponse products = productsDAO.searchProducts(queryBuilder, 0, 10);

        assertEquals(0, products.getProducts().size());
    }

    @Test
    public void searchByPraseTest() throws IOException, InterruptedException {
        DBProduct product = DBProductFixture.create(UUID.randomUUID().toString());
        product.setName("Dress");
        productsDAO.saveProduct(product);
        Thread.sleep(1000);
        SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
        queryBuilder.setPhrase("Dress");

        DBProductListResponse products = productsDAO.searchProducts(queryBuilder, 0, 10);
        Iterator<DBProduct> it = products.getProducts().iterator();
        DBProduct dbProduct = new DBProduct();
        while (it.hasNext()) {
            dbProduct = it.next();
        }
        DBProductFixture.check(dbProduct, product);
    }

    @Test
    public void searchByFullSearchQueryTest() throws IOException, InterruptedException {

        DBProduct product = DBProductFixture.create(UUID.randomUUID().toString());
        product.setBrand("3");
        product.setCluster(0L);
        product.setTopCategory("outerwear");
        List<String> size = new ArrayList<>();
        size.add("10");
        product.setSize(size);
        List<String> colour = new ArrayList<>();
        colour.add("verylightgrey");
        product.setColor(colour);

        productsDAO.saveProduct(product);
        Thread.sleep(1000);
        
        SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
        Collection<String> productIds = new ArrayList<>();
        productIds.add(product.getId());
        queryBuilder.addProductIds(productIds);
        Collection<String> brandIds = new ArrayList<>();
        brandIds.add("3");
        queryBuilder.addBrandIds(brandIds);
        Collection<Integer> clusters = new ArrayList<>();
        clusters.add(0);
        queryBuilder.addClusters(clusters);
        Collection<String> sizes = new ArrayList<>();
        sizes.add("10");
        queryBuilder.addSizes(sizes);
        Collection<String> topCategories = new ArrayList<>();
        topCategories.add("outerwear");
        queryBuilder.addTopCategories(topCategories);
        queryBuilder.setGender(0);
        Collection<String> colours = new ArrayList<>();
        colours.add("white");
        colours.add("verylightgrey");
        queryBuilder.addColours(colours);

        DBProductListResponse products = productsDAO.searchProducts(queryBuilder, 0, 10);
        DBProduct dbProduct = getProductFromSet(products);

        DBProductFixture.check(dbProduct, product);
    }

    private DBProduct getProductFromSet(DBProductListResponse products) {
        Iterator<DBProduct> it = products.getProducts().iterator();
        DBProduct savedDBProduct = null;
        while (it.hasNext()) {
            savedDBProduct = it.next();
        }
        return savedDBProduct;
    }
}