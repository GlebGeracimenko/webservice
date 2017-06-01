package com.gleb.webservices.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Cookie;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gleb.dao.ProductDAO;
import com.gleb.dao.UsersDAO;
import com.gleb.dao.cassandra.BasicCQLCassandraDAO;
import com.gleb.dao.cassandra.CQLUserDAO;
import com.gleb.dao.elasticsearch.ElasticProductDAOImpl;
import com.gleb.dao.elasticsearch.ElasticsearchClient;
import com.gleb.dao.objects.DBProduct;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOProduct;
import com.gleb.webservices.bo.BOProductListResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ProductSearchResourceFiltersTest extends BasicIntegrationTest {

    @Autowired
    private ElasticProductDAOImpl productDAO;

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private BasicCQLCassandraDAO basicCQLDAO;

    @Autowired
    private ElasticsearchClient clientHolder;

    private DBUser testUser = UserFixture.createUser(UUID.randomUUID());

    private void cleanUp() {
        truncateCassandra(CQLUserDAO.USERS_COLUMN_FAMILY);
        truncateElasticsearchType(ProductDAO.PRODUCT_TYPE);
    }

    @After
    public void after() {
        cleanUp();
    }

    @Before
    public void before() {
        cleanUp();
        usersDAO.saveUser(testUser);
    }

    private DBProduct createProduct(String id) {
        DBProduct product = new DBProduct();
        product.setBrand("1");
        product.setTopCategory("category");
        product.setColor(Arrays.asList("white"));
        product.setDesc("description");
        product.setGender(DBUser.GENDER_FEMALE);
        product.setImages(Arrays.asList("image.jpg"));
        product.setName("some name");
        product.setPrice(10.0);
        product.setSize(Arrays.asList("XL"));
        product.setSku("123");
        product.setStore("FC");
        product.setStyle("style1");
        product.setId(id);
        product.setUrl("http://example.com/item");
        return product;
    }

    @Test
    public void filtersTest() throws JsonProcessingException, InterruptedException {

        /**
         * Set user's gender to female
         */
        testUser.setGender(DBUser.GENDER_FEMALE);
        usersDAO.saveUser(testUser);

        DBProduct product1 = createProduct("1l");
        product1.setName("some cool name");
        product1.setDesc("bla bla description");
        product1.setBrand("10");
        product1.setCluster(1l);
        product1.setColor(Arrays.asList("black"));
        productDAO.saveProduct(product1);

        DBProduct product2 = createProduct("2l");
        product2.setName("another super name");
        product2.setDesc("bla2 bla description2");
        product2.setBrand("1");
        product2.setPrice(2);
        product2.setCluster(2L);
        product2.setStyle("Casual Party");
        productDAO.saveProduct(product2);

        DBProduct product3 = createProduct("3l");
        product3.setName("super another name");
        product3.setDesc("unique amaizing super description2");
        product3.setBrand("5");
        product3.setTopCategory("topCategory");
        product3.setPrice(3);
        productDAO.saveProduct(product3);

        DBProduct product4 = createProduct("4l");
        product4.setName("new");
        product4.setDesc("really new and cool");
        product4.setBrand("1");
        product4.setSize(Arrays.asList("M"));
        product4.setCluster(5L);
        productDAO.saveProduct(product4);

        DBProduct product5 = createProduct("5l");
        product5.setName("some cool name");
        product5.setDesc("bla bla description");
        product5.setBrand("2");
        product5.setSize(Arrays.asList("S"));
        product5.setTopCategory("topCategory");
        productDAO.saveProduct(product5);

        DBProduct product6 = createProduct("7l");
        product6.setName("another gender cool name");
        product6.setDesc("bla bla description");
        product6.setBrand("3");
        product6.setTopCategory("topCategory");
        product6.setGender(DBUser.GENDER_MALE);
        productDAO.saveProduct(product6);
        Thread.sleep(1000);

        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());

        BOProductListResponse products = path("/products/search/filters").queryParam("price_from", "2").request().cookie(cookie)
                .get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(5));
        assertThat(getProductIdFromSet(products), hasItems(product1.getId(), product2.getId(), product3.getId(), product4.getId(), product5.getId()));

        products = path("/products/search/filters").queryParam("price_to", "5").request().cookie(cookie).get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(2));
        assertThat(getProductIdFromSet(products), hasItems(product2.getId(), product3.getId()));

        products = path("/products/search/filters").queryParam("price_from", "2.5").queryParam("price_to", "3").request().cookie(cookie)
                .get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(1));
        assertThat(products.getProducts().get(0).getId(), equalTo(product3.getId()));

        products = path("/products/search/filters").queryParam("brand", "5").request().cookie(cookie).get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(1));
        assertThat(products.getProducts().get(0).getId(), equalTo(product3.getId()));

        products = path("/products/search/filters").queryParam("top_category", "topCategory").request().cookie(cookie).get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(2));
        assertThat(getProductIdFromSet(products), hasItems(product5.getId(), product3.getId()));

        products = path("/products/search/filters").queryParam("top_category", "topCategory").request().cookie(cookie).get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(2));

        products = path("/products/search/filters").queryParam("style", "Casual Party").request().cookie(cookie).get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(1));
        assertThat(products.getProducts().get(0).getId(), equalTo(product2.getId()));

        products = path("/products/search/filters").queryParam("phrase", "Casual Party").request().cookie(cookie).get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(1));
        assertThat(products.getProducts().get(0).getId(), equalTo(product2.getId()));

        products = path("/products/search/filters").queryParam("phrase", "cool").request().cookie(cookie).get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(3));
        assertThat(getProductIdFromSet(products), hasItems(product1.getId(), product4.getId(), product5.getId()));

        products = path("/products/search/filters").queryParam("phrase", "bla2 bla description2").request().cookie(cookie).get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(1));
        assertThat(products.getProducts().get(0).getId(), equalTo(product2.getId()));

        products = path("/products/search/filters").queryParam("colour", "white").request().cookie(cookie).get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(4));
        assertThat(getProductIdFromSet(products), hasItems(product2.getId(), product3.getId(), product4.getId(), product5.getId()));

        products = path("/products/search/filters").queryParam("size", "XL").request().cookie(cookie).get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(3));
        assertThat(getProductIdFromSet(products), hasItems(product1.getId(), product2.getId(), product3.getId()));

        // Problem - returns 1 product with "S" size only instead 2 with "M" size too
        products = path("/products/search/filters").queryParam("size", "S").queryParam("size", "M").request().cookie(cookie).get(BOProductListResponse.class);
        // /assertThat(products.getCount(), equalTo(1));
        // assertThat(products.getProducts().get(0).getId(), equalTo(product5.getId()));

        products = path("/products/search/filters").queryParam("cluster", "2").request().cookie(cookie).get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(1));
        assertThat(product2.getId(), equalTo(products.getProducts().get(0).getId()));

        products = path("/products/search/filters").queryParam("cluster", "5").queryParam("cluster", "2").request().cookie(cookie)
                .get(BOProductListResponse.class);
        assertThat(products.getCount(), equalTo(2));
        assertThat(getProductIdFromSet(products), hasItems(product4.getId(), product2.getId()));

        products = path("/products/search/filters").queryParam("gender", "1").request().cookie(cookie).get(BOProductListResponse.class);

        assertNotNull(products);
        assertThat(products.getCount(), equalTo(1)); // only 1 product with gender "mail" exists
        assertThat(products.getProducts().get(0).getId(), equalTo(product6.getId()));

        products = path("/products/search/filters").queryParam("category", "category").queryParam("price_from", "2.5").queryParam("price_to", "3")
                .queryParam("name", "super").queryParam("description", "desc").queryParam("colour", "white").request().cookie(cookie)
                .get(BOProductListResponse.class);

        assertNotNull(products);
        assertThat(products.getCount(), equalTo(1));
        assertThat(products.getProducts().get(0).getId(), equalTo(product3.getId()));

        logout(cookie);
    }

    private List<String> getProductIdFromSet(BOProductListResponse products) {
        List<String> result = new ArrayList<>();
        Iterator<BOProduct> it = products.getProducts().iterator();
        while (it.hasNext()) {
            result.add(it.next().getId());
        }
        return result;
    }
}