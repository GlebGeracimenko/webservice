package com.gleb.webservices.bo;

import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.datastax.driver.mapping.annotations.Column;

@XmlRootElement
public class BOFullWishlist {
    @Column(name = "id")
    private String userId;
    private Set<BOProduct> likedProducts;
    private Set<BOProduct> dislikedProducts;
    private Map<String, BOBrandWithFollowing> brands;

    public Map<String, BOBrandWithFollowing> getBrands() {
        return brands;
    }

    public void setBrands(Map<String, BOBrandWithFollowing> brands) {
        this.brands = brands;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<BOProduct> getLikedProducts() {
        return likedProducts;
    }

    public void setLikedProducts(Set<BOProduct> lideProducts) {
        this.likedProducts = lideProducts;
    }

    public Set<BOProduct> getDislikedProducts() {
        return dislikedProducts;
    }

    public void setDislikedProducts(Set<BOProduct> dislikedProducts) {
        this.dislikedProducts = dislikedProducts;
    }
}
