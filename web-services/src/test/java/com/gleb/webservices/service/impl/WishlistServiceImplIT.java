//package com.maistylz.webservices.service.impl;
//
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.junit.Assert.assertThat;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.ContextHierarchy;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import BasicCQLCassandraDAO;
//import CQLWishlistsDAOImpl;
//import ElasticProductDAOImpl;
//import ElasticsearchClient;
//import BOFullWishlist;
//import BOProduct;
//import ProductMapper;
//import WishlistService;
//import BOProductFixture;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
//public class WishlistServiceImplIT {
//
//    @Autowired
//    private ElasticProductDAOImpl productDAO;
//
//    @Autowired
//    private ElasticsearchClient clientHolder;
//
//    @Autowired
//    private BasicCQLCassandraDAO basicCassandraDAO;
//
//    @Autowired
//    private ProductMapper productMapper;
//
//    @Autowired
//    private WishlistService wishlistService;
//
//    public void cleanUp() {
//         basicCassandraDAO.getSession().execute("truncate " + CQLWishlistsDAOImpl.WISHLISTS_COLUMN_FAMILY);
////        clientHolder.getClient().prepareDeleteByQuery(clientHolder.getIndexName()).setQuery(QueryBuilders.matchAllQuery())
////                .setTypes(ElasticProductDAOImpl.PRODUCT_TYPE).execute().actionGet();
//    }
//
//    @After
//    public void after() {
//        cleanUp();
//    }
//
//    @Before
//    public void before() {
//        cleanUp();
//    }
//
//    @Test
//    public void test_getAll() throws Exception {
//        BOProduct boProduct1 = BOProductFixture.createBOProduct(1L);
//        BOProduct boProduct2 = BOProductFixture.createBOProduct(2L);
//        BOProduct boProduct3 = BOProductFixture.createBOProduct(3L);
//        productDAO.saveProduct(productMapper.map(boProduct1));
//        productDAO.saveProduct(productMapper.map(boProduct2));
//        productDAO.saveProduct(productMapper.map(boProduct3));
//        // Tricky hack
//        Thread.sleep(2000);
//        wishlistService.addLikedItem(1L, 1L);
//        wishlistService.addLikedItem(1L, 2L);
//        wishlistService.addLikedItem(1L, 3L);
//
//        BOFullWishlist boFullWishlist = wishlistService.getFullWishlist(1L);
//        assertThat(boFullWishlist.getLikedProducts().size(), equalTo(3));
//        assertThat(boFullWishlist.getDislikedProducts().size(), equalTo(0));
//
//        wishlistService.addDislikedItem(1L, 3L);
//
//        boFullWishlist = wishlistService.getFullWishlist(1L);
//        assertThat(boFullWishlist.getLikedProducts().size(), equalTo(2));
//        assertThat(boFullWishlist.getDislikedProducts().size(), equalTo(1));
//
//        wishlistService.addDislikedItem(1L, 2L);
//
//        boFullWishlist = wishlistService.getFullWishlist(1L);
//        assertThat(boFullWishlist.getLikedProducts().size(), equalTo(1));
//        assertThat(boFullWishlist.getDislikedProducts().size(), equalTo(2));
//        
//        wishlistService.addDislikedItem(1L, 1L);
//
//        boFullWishlist = wishlistService.getFullWishlist(1L);
//        assertThat(boFullWishlist.getLikedProducts().size(), equalTo(0));
//        assertThat(boFullWishlist.getDislikedProducts().size(), equalTo(3));
//
//    }
//}
