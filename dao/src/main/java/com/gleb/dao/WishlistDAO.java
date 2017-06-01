package com.gleb.dao;

import java.util.UUID;

import com.gleb.dao.objects.DBWishlist;

/**
 * Declares interface for wishlists manipulations.
 * 
 * @author Khamylov Oleksandr
 * @author Viacheslav Vasianovych
 */
public interface WishlistDAO {

    DBWishlist getWishlist(UUID userId);

    void addItemToWishlist(UUID userId, String itemId, String type);
}
