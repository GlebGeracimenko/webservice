package com.gleb.webservices.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import com.gleb.webservices.test.fixtures.BOProductFixture;
import com.gleb.webservices.test.fixtures.UserFixture;
import com.gleb.webservices.tests.BasicIntegrationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gleb.dao.ColourDAO;
import com.gleb.dao.ProductDAO;
import com.gleb.dao.UsersDAO;
import com.gleb.dao.cassandra.BasicCQLCassandraDAO;
import com.gleb.dao.cassandra.CQLUserDAO;
import com.gleb.dao.elasticsearch.ElasticsearchClient;
import com.gleb.dao.objects.DBProduct;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOProduct;
import com.gleb.webservices.bo.BOProductListResponse;
import com.gleb.webservices.mapping.ProductMapper;
import com.gleb.webservices.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ProductServiceImplIT extends BasicIntegrationTest {

    @Autowired
    private ProductService serviceImpl;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ColourDAO colourDAO;

    @Autowired
    private ElasticsearchClient clientHolder;

    @Autowired
    private BasicCQLCassandraDAO basicCassandraDAO;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ElasticsearchClient client;

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private BasicCQLCassandraDAO basicCQLDAO;

    private DBUser testUser = UserFixture.createUser(UUID.randomUUID());

    private void cleanUp() {
        basicCQLDAO.getSession().execute("truncate " + CQLUserDAO.USERS_COLUMN_FAMILY);
        truncateElasticsearchType(ProductDAO.PRODUCT_TYPE);
    }

    @After
    public void after() {
        cleanUp();
    }

    @Before
    public void before() throws IOException {
        cleanUp();
        usersDAO.saveUser(testUser);
    }

    @Test
    public void testSave() throws Exception {
        BOProduct boProduct = BOProductFixture.createBOProduct(null);
        String id = serviceImpl.saveProduct(boProduct);

        assertNotNull(id);
        DBProduct product = productDAO.getById(id);
        BOProductFixture.check(product, boProduct);
    }

    @Test
    public void testGetProduct() throws Exception {
        BOProduct boProduct = BOProductFixture.createBOProduct(null);
        String id = serviceImpl.saveProduct(boProduct);

        assertNotNull(id);
        BOProduct product = serviceImpl.getProduct(id);
        BOProductFixture.check(product, boProduct);
    }

    @Test
    public void testGetProducts() throws Exception {
        Collection<String> ids = new ArrayList<>();
        BOProduct boProduct1 = BOProductFixture.createBOProduct(null);
        String id1 = serviceImpl.saveProduct(boProduct1);
        ids.add(id1);
        BOProduct boProduct2 = BOProductFixture.createBOProduct(null);
        String id2 = serviceImpl.saveProduct(boProduct2);
        ids.add(id2);
        BOProduct boProduct3 = BOProductFixture.createBOProduct(null);
        String id3 = serviceImpl.saveProduct(boProduct3);
        ids.add(id3);
        BOProduct boProduct4 = BOProductFixture.createBOProduct(null);
        String id4 = serviceImpl.saveProduct(boProduct4);
        ids.add(id4);

        assertNotNull(id1);
        assertNotNull(id2);
        assertNotNull(id3);
        assertNotNull(id4);
        assertFalse(id1.equals(id3));
        assertFalse(id2.equals(id4));
        BOProduct product = serviceImpl.getProduct(id1);

        BOProductFixture.check(product, boProduct1);

        BOProductListResponse allProducts = serviceImpl.getProducts(ids, testUser.getId());
        assertEquals(allProducts.getProducts().size(), 4);
    }

    // //TESTS WITH BATCHES
    //
    // @Test
    // public void testGetProductsByTopCategoryStyleAndPrice() throws Exception {
    // //Batch
    // storeBatchElastic(ProductDAO.PRODUCT_TYPE, "ProductsResourceIt-batch1.json");
    //
    // ArrayList<Integer> clusters = new ArrayList<>();
    // clusters.add(0);
    // Set<BOProduct> products = serviceImpl.getProductsByTopCategoryStyleAndPrice("outerwear", null, clusters, 0);
    // //Thread.sleep(1000);
    // assertEquals(3, products.size()); // ??
    // cleanUp();
    // }
    //
    // @Test
    // public void testGetProductsByPriceRecommendation() throws Exception {
    // //Batch
    // storeBatchElastic(ProductDAO.PRODUCT_TYPE, "ProductsResourceIt-batch1.json");
    //
    // ArrayList<Integer> clusters = new ArrayList<>();
    // clusters.add(2);
    // Set<BOProduct> products = serviceImpl.getProductsByPriceRecommendation("outerwear", clusters, 0);
    // Thread.sleep(1000);
    // assertEquals(1, products.size()); // ??
    //
    // cleanUp();
    // }
    //
    // @Test
    // public void testGetProductsByTopCategoryAndColours() throws Exception {
    // //Batch
    // storeBatchElastic(ProductDAO.PRODUCT_TYPE, "ProductsResourceIt-batch1.json");
    // storeBatchElastic(ColourDAO.COLOUR_TYPE, "Colours-batch.json");
    //
    // List<Long> colours = new ArrayList<>();
    // colours.add(4L);
    // Set<BOProduct> products = serviceImpl.getProductsByTopCategoryAndColours("outerwear", colours, testUser, 0);
    // Thread.sleep(1000);
    // assertEquals(1, products.size()); // ?
    //
    // cleanUp();
    // }
}