package com.gleb.dao.objects;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * DB Object represents wishlist entity.
 * 
 * @author Khamylov Oleksandr
 * @author Viacheslav Vasianovych
 */
public class DBWishlist {

    private UUID id;

    private Set<String> likedItems = new HashSet<String>();

    private Set<String> dislikedItems = new HashSet<String>();

    public Set<String> getLikedItems() {
        return likedItems;
    }

    public void setLikedItems(Set<String> likedItems) {
        this.likedItems = likedItems;
    }

    public Set<String> getDislikedItems() {
        return dislikedItems;
    }

    public void setDislikedItems(Set<String> dislikedItems) {
        this.dislikedItems = dislikedItems;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
