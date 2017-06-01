package com.gleb.dao.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchQueryBuilder {
    private int gender = 0;
    private String phrase = "";
    private Double priceTo;
    private Double priceFrom;

    private List<String> brandIds = new ArrayList<>();

    private List<String> brandIdsExclude = new ArrayList<>();

    private List<String> productIds = new ArrayList<>();

    private List<String> productIdsExclude = new ArrayList<>();

    private List<String> topCategories = new ArrayList<>();

    private List<String> colours = new ArrayList<>();

    private List<String> styles = new ArrayList<>();

    private List<Integer> clusters = new ArrayList<>();

    private List<String> sizes = new ArrayList<>();

    public Double getPriceFrom() {
        return priceFrom;
    }

    public Double getPriceTo() {
        return priceTo;
    }

    public int getGender() {
        return gender;
    }

    public String getPhrase() {
        return phrase;
    }

    public List<String> getBrandIds() {
        return brandIds;
    }

    public List<String> getBrandIdsExclude() {
        return brandIdsExclude;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public List<String> getProductIdsExclude() {
        return productIdsExclude;
    }

    public List<String> getTopCategories() {
        return topCategories;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public List<String> getColours() {
        return colours;
    }

    public List<Integer> getClusters() {
        return clusters;
    }

    public List<String> getStyles() {
        return styles;
    }

    public SearchQueryBuilder setPhrase(String phrase) {
        if (phrase != null)
            this.phrase = phrase.toLowerCase();
        return this;
    }

    public SearchQueryBuilder addColours(Collection<String> colours) {
        this.colours.addAll(colours);
        return this;
    }

    public SearchQueryBuilder addClusters(Collection<Integer> clusters) {
        this.clusters.addAll(clusters);
        return this;
    }

    public SearchQueryBuilder addTopCategories(Collection<String> topCategories) {
        this.topCategories.addAll(topCategories);
        return this;
    }

    public SearchQueryBuilder addProductIds(Collection<String> productIds) {
        this.productIds.addAll(productIds);
        return this;
    }

    public SearchQueryBuilder addExcludedProductIds(Collection<String> productIds) {
        this.productIdsExclude.addAll(productIds);
        return this;
    }

    public SearchQueryBuilder addSizes(Collection<String> sizes) {
        for (String size : sizes) {
            this.sizes.add(size.toLowerCase());
        }
        return this;
    }

    public SearchQueryBuilder addStyles(Collection<String> styles) {
        for (String style : styles) {
            this.styles.add(style.toLowerCase());
        }
        return this;
    }

    public SearchQueryBuilder addBrandIds(Collection<String> brandIds) {
        this.brandIds.addAll(brandIds);
        return this;
    }

    public SearchQueryBuilder addExcludedBrandIds(Collection<String> brandIds) {
        this.brandIdsExclude.addAll(brandIds);
        return this;
    }

    public SearchQueryBuilder setGender(int gender) {
        this.gender = gender;
        return this;
    }

    public SearchQueryBuilder addPriceFrom(Double priceFrom) {
        this.priceFrom = priceFrom;
        return this;
    }

    public SearchQueryBuilder addPriceTo(Double priceTo) {
        this.priceTo = priceTo;
        return this;
    }
}
