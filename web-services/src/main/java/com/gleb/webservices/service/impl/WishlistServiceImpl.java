package com.gleb.webservices.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gleb.dao.WishlistDAO;
import com.gleb.dao.cassandra.CQLLikesDAO;
import com.gleb.dao.objects.DBWishlist;
import com.gleb.webservices.bo.BOBrandWithFollowing;
import com.gleb.webservices.bo.BOFullWishlist;
import com.gleb.webservices.bo.BOProduct;
import com.gleb.webservices.bo.BOProductListResponse;
import com.gleb.webservices.service.ProductService;
import com.gleb.webservices.service.WishlistService;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistDAO wishlistDAO;

    @Autowired
    private ProductService productService;

    @Override
    public Set<String> getLikedProductIdsForUser(UUID userId) {
        DBWishlist dbWishlist = wishlistDAO.getWishlist(userId);
        if (dbWishlist == null) {
            dbWishlist = new DBWishlist();
            dbWishlist.setId(userId);
        }
        return dbWishlist.getLikedItems();
    }

    @Override
    public Set<String> getDislikedProductIdsForUser(UUID userId) {
        DBWishlist dbWishlist = wishlistDAO.getWishlist(userId);
        if (dbWishlist == null) {
            dbWishlist = new DBWishlist();
            dbWishlist.setId(userId);
        }
        return dbWishlist.getDislikedItems();
    }

    @Override
    public void addLikedItem(UUID userId, String productId) {
        wishlistDAO.addItemToWishlist(userId, productId, CQLLikesDAO.LIKE_TYPE);
    }

    @Override
    public void addDislikedItem(UUID userId, String productId) {
        wishlistDAO.addItemToWishlist(userId, productId, CQLLikesDAO.DISLIKE_TYPE);
    }

    @Override
    public BOFullWishlist getFullWishlist(UUID userId) {
        DBWishlist dbWishlist = wishlistDAO.getWishlist(userId);
        if (dbWishlist == null) {
            dbWishlist = new DBWishlist();
            dbWishlist.setId(userId);
        }
        Set<String> likedProductsIds = dbWishlist.getLikedItems();
        BOProductListResponse likedProducts = productService.getProducts(likedProductsIds, userId);
        Set<String> dislikedProductsIds = dbWishlist.getDislikedItems();
        BOProductListResponse dislikedProducts = productService.getProducts(dislikedProductsIds, userId);
        BOFullWishlist wishlist = new BOFullWishlist();
        wishlist.setUserId(userId.toString());
        wishlist.setLikedProducts(new HashSet<BOProduct>(likedProducts.getProducts()));
        wishlist.setDislikedProducts(new HashSet<BOProduct>(dislikedProducts.getProducts()));
        Map<String, BOBrandWithFollowing> brands = new HashMap<String, BOBrandWithFollowing>();
        brands.putAll(likedProducts.getBrands());
        brands.putAll(dislikedProducts.getBrands());
        wishlist.setBrands(brands);
        return wishlist;
    }
}
