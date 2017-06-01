package com.gleb.webservices.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gleb.dao.FollowsDAO;
import com.gleb.dao.UsersDAO;
import com.gleb.dao.cassandra.BasicCQLCassandraDAO;
import com.gleb.dao.cassandra.CQLFollowDAOImpl;
import com.gleb.dao.cassandra.CQLUserDAO;
import com.gleb.dao.cassandra.CQLWishlistsDAOImpl;
import com.gleb.dao.elasticsearch.ElasticBrandsDAOImpl;
import com.gleb.dao.elasticsearch.ElasticProductDAOImpl;
import com.gleb.dao.elasticsearch.ElasticsearchClient;
import com.gleb.dao.objects.DBProduct;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOBrand;
import com.gleb.webservices.bo.BOProduct;
import com.gleb.webservices.bo.BOProductListResponse;
import com.gleb.webservices.service.BrandsService;
import com.gleb.webservices.service.LikesService;
import com.gleb.webservices.service.WishlistService;
import com.gleb.webservices.test.fixtures.UserFixture;
import com.gleb.webservices.tests.BasicIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ProductSearchResourceTest extends BasicIntegrationTest {

    @Autowired
    private ElasticProductDAOImpl itemsDAO;

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private BasicCQLCassandraDAO basicCQLDAO;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private FollowsDAO followsDAO;

    @Autowired
    private BrandsService brandsService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private ElasticsearchClient clientHolder;

    private DBUser testUser = UserFixture.createUser(UUID.randomUUID());

    private void cleanUp() {
        truncateCassandra(CQLUserDAO.USERS_COLUMN_FAMILY);
        truncateCassandra(CQLWishlistsDAOImpl.WISHLISTS_COLUMN_FAMILY);
        truncateCassandra(CQLFollowDAOImpl.FOLLOWS_USERS_COLUMN_FAMILY);
        truncateCassandra(CQLFollowDAOImpl.USERS_FOLLOWS_COLUMN_FAMILY);
        truncateElasticsearchType(ElasticProductDAOImpl.PRODUCT_TYPE);
        truncateElasticsearchType(ElasticBrandsDAOImpl.BRAND_TYPE);
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
    public void testAboutLikedAndDislikedItems() throws JsonProcessingException, InterruptedException {
        testUser.setGender(DBUser.GENDER_FEMALE);
        usersDAO.saveUser(testUser);

        /**
         * new brand
         */
        BOBrand brand = new BOBrand();
        brand.setDescription("desc");
        brand.setId("1L");
        brand.setName("stub");
        brand.setUrl("http://example.com");
        brandsService.saveBrand(brand);
        followsDAO.follow(testUser.getId(), "1L", FollowsDAO.TYPE_BRAND);
        Thread.sleep(3000);

        DBProduct product1 = createProduct("1L");
        product1.setName("jacket");
        product1.setDesc("jacket description");
        product1.setBrand("1");
        product1.setGender(DBUser.GENDER_FEMALE);
        itemsDAO.saveProduct(product1);

        DBProduct product2 = createProduct("2L");
        product2.setName("super jacket");
        product2.setDesc("cool description");
        product2.setBrand("1");
        product2.setGender(DBUser.GENDER_FEMALE);
        itemsDAO.saveProduct(product2);

        DBProduct product3 = createProduct("3L");
        product3.setName("military jacket");
        product3.setDesc("description about militaryt jacket");
        product3.setBrand("1");
        product3.setGender(DBUser.GENDER_FEMALE);
        itemsDAO.saveProduct(product3);

        DBProduct product4 = createProduct("5L");
        product4.setName("some cool name");
        product4.setDesc("bla bla description");
        product4.setBrand("2");
        product4.setTopCategory("top");
        itemsDAO.saveProduct(product4);
        Thread.sleep(1000);

        Thread.sleep(3000);
        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());
        BOProductListResponse products = path("/products/search").queryParam("text", "jacket").request().cookie("JSESSIONID", cookie.getValue())
                .get(BOProductListResponse.class);

        assertNotNull(products);
        assertThat(products.getProducts().size(), equalTo(3));

        likesService.likeProduct(product1.getId(), testUser.getId());
        wishlistService.addLikedItem(testUser.getId(), product1.getId());
        products = path("/products/search").queryParam("text", "jacket").request().cookie(cookie).get(BOProductListResponse.class);

        assertNotNull(products);
        assertThat(products.getProducts().size(), equalTo(2));
        assertThat(products.getProducts().get(0).getId(), not(equalTo(product1.getId())));
        assertThat(products.getProducts().get(1).getId(), not(equalTo(product1.getId())));

        likesService.dislikeProduct(product2.getId(), testUser.getId());
        wishlistService.addDislikedItem(testUser.getId(), product2.getId());
        products = path("/products/search").queryParam("text", "military").request().cookie(cookie).get(BOProductListResponse.class);
        assertNotNull(products);
        assertThat(products.getProducts().size(), equalTo(1));
        assertThat(products.getProducts().get(0).getId(), not(equalTo(product2.getId())));
        assertThat(products.getProducts().get(0).getId(), not(equalTo(product2.getId())));
        assertThat(products.getProducts().get(0).getId(), equalTo(product3.getId()));

        products = path("/products/search").queryParam("text", "super").request().cookie(cookie).get(BOProductListResponse.class);

        assertThat(products.getProducts().size(), equalTo(0));

        logout(cookie);
    }

    @Test
    public void testSearchForProductByRootCategory() throws JsonProcessingException, InterruptedException {
        DBProduct product1 = createProduct("2L");
        product1.setName("super jacket");
        product1.setDesc("cool description");
        product1.setBrand("1");
        product1.setTopCategory("bottom");
        product1.setGender(DBUser.GENDER_FEMALE);
        itemsDAO.saveProduct(product1);

        DBProduct product2 = createProduct("5L");
        product2.setName("some cool name");
        product2.setDesc("bla bla description");
        product2.setBrand("2");
        product2.setTopCategory("top");
        itemsDAO.saveProduct(product2);
        Thread.sleep(1000);

        Cookie cookie = login(testUser.getLogin(), testUser.getPassword());

        Set<BOProduct> boProducts1 = path("/products/search/byCategory").queryParam("topCategory", "top").queryParam("text", "cool").request().cookie(cookie)
                .get(new GenericType<Set<BOProduct>>() {
                });
        assertEquals(boProducts1.size(), 1);

        Set<BOProduct> boProducts2 = path("/products/search/byCategory").queryParam("text", "cool").request().cookie(cookie)
                .get(new GenericType<Set<BOProduct>>() {
                });
        assertEquals(boProducts2.size(), 2);

        Set<BOProduct> boProducts3 = path("/products/search/byCategory").queryParam("topCategory", "cool").request().cookie(cookie)
                .get(new GenericType<Set<BOProduct>>() {
                });
        assertEquals(boProducts3.size(), 0);

        Set<BOProduct> boProducts4 = path("/products/search/byCategory").queryParam("topCategory", "top").request().cookie(cookie)
                .get(new GenericType<Set<BOProduct>>() {
                });
        assertEquals(boProducts4.size(), 1);

        logout(cookie);
    }
}