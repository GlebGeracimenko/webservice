package com.gleb.webservices.service;

import java.util.Set;
import java.util.UUID;

import com.gleb.webservices.bo.BOFullWishlist;

public interface WishlistService {
    
    Set<String> getLikedProductIdsForUser(UUID userId);

    Set<String> getDislikedProductIdsForUser(UUID userId);

    void addLikedItem(UUID userId, String productId);

    void addDislikedItem(UUID userId, String productId);
    
    BOFullWishlist getFullWishlist(UUID userId);
}
