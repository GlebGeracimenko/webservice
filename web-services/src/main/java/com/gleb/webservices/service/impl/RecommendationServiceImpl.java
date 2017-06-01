package com.gleb.webservices.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gleb.dao.BrandsDAO;
import com.gleb.dao.ProductDAO;
import com.gleb.dao.WishlistDAO;
import com.gleb.dao.objects.DBUser;
import com.gleb.dao.objects.DBWishlist;
import com.gleb.webservices.bo.BOProduct;
import com.gleb.webservices.bo.BOProductListResponse;
import com.gleb.webservices.mapping.BrandsMapper;
import com.gleb.webservices.service.BrandsService;
import com.gleb.webservices.service.ProductService;
import com.gleb.webservices.service.RecommendationService;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private ProductService productService;

    @Autowired
    private WishlistDAO wishlistDAO;

    @Autowired
    private BrandsService brandsService;

    @Autowired
    private BrandsDAO brandsDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private BrandsMapper brandsMapper;

    @Override
    public BOProductListResponse getRecomendations(DBUser dbuser) {
        UUID userId = dbuser.getId();
        DBWishlist wishlist = wishlistDAO.getWishlist(userId);
        List<String> excludedProducts = new ArrayList<String>();
        if (wishlist != null) {
            excludedProducts.addAll(wishlist.getDislikedItems());
            excludedProducts.addAll(wishlist.getLikedItems());
        }
        Collection<String> followingBrands = brandsService.getAllBrandIdsFollowedByUser(dbuser.getId());
        BOProductListResponse newResponse = productService.searchProducts("", dbuser, 0, 10);
        for (String brandId : newResponse.getBrands().keySet()) {
            if (followingBrands.contains(brandId)) {
                newResponse.getBrands().get(brandId).setFollowing(true);
            }
        }
        newResponse.setCount(newResponse.getProducts().size());
        return newResponse;
    }

    @Override
    public Set<BOProduct> getRecommendationsByTopCategoryAndColoursIds(List<String> colours, String topCategory, DBUser dbUser) {
        return productService.getProductsByTopCategoryAndColours(topCategory, colours, dbUser, 0);
    }

    @Override
    public Set<BOProduct> getPriceRecommendations(DBUser dbUser) {
        DBWishlist wishlist = wishlistDAO.getWishlist(dbUser.getId());
        Map<String, Set<Integer>> priceDistributionByCategories = splitLikedItemsByPriceClusters(wishlist.getLikedItems());
        Set<BOProduct> searchList = new HashSet<BOProduct>();
        for (String topCategory : priceDistributionByCategories.keySet()) {
            searchList.addAll(productService.getProductsByPriceRecommendation(topCategory,
                    new ArrayList<Integer>(priceDistributionByCategories.get(topCategory)), 0));
        }
        if (wishlist.getLikedItems() != null)
            searchList.removeAll(wishlist.getLikedItems());
        if (wishlist.getDislikedItems() != null)
            searchList.removeAll(wishlist.getDislikedItems());
        return searchList;
    }

    @Override
    public Set<BOProduct> getPriceRecommendationsByTopCategoryAndStyle(String topCategory, String style, DBUser dbUser) {
        DBWishlist wishlist = wishlistDAO.getWishlist(dbUser.getId());
        Set<Integer> priceClusters = new TreeSet<>();
        for (String productId : wishlist.getLikedItems()) {
            priceClusters.add(productService.getProduct(productId).getCluster());
        }
        Set<BOProduct> searchList = new HashSet<BOProduct>();
        searchList.addAll(productService.getProductsByTopCategoryStyleAndPrice(topCategory, style, new ArrayList<Integer>(priceClusters), 0));
        if (wishlist.getLikedItems() != null)
            searchList.removeAll(wishlist.getLikedItems());
        if (wishlist.getDislikedItems() != null)
            searchList.removeAll(wishlist.getDislikedItems());
        return searchList;
    }

    private Map<String, Set<Integer>> splitLikedItemsByPriceClusters(Set<String> likedItems) {
        Map<String, Set<Integer>> priceDistribution = new HashMap<>();
        for (String productId : likedItems) {
            BOProduct product = productService.getProduct(productId);
            String category = product.getCategory();
            if (priceDistribution.containsKey(category)) {
                priceDistribution.put(category, new TreeSet<Integer>());
                priceDistribution.get(category).add(product.getCluster());
            } else
                priceDistribution.get(category).add(product.getCluster());
        }
        return priceDistribution;
    }
}
