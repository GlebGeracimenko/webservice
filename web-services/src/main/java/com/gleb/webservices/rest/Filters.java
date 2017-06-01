package com.gleb.webservices.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.QueryParam;

/**
 * @author Viacheslav Vasianovych
 */

public class Filters {

    @QueryParam("top_category")
    private List<String> top_category = new ArrayList<>();

    @QueryParam("gender")
    private int gender = 0;

    @QueryParam("phrase")
    private String phrase = "";

    @QueryParam("brand")
    private List<String> brands = new ArrayList<>();

    @QueryParam("colour")
    private List<String> colours = new ArrayList<>();

    @QueryParam("size")
    private List<String> size = new ArrayList<>();
    
    @QueryParam("style")
    private List<String> style = new ArrayList<>(); 

    @QueryParam("price_from")
    private Double priceFrom;

    @QueryParam("price_to")
    private Double priceTo;

    @QueryParam("cluster")
    private List<Integer> cluster;

    public List<String> getTopCategory() {
        return top_category;
    }

    public void setTopCategory(List<String> top_category) {
        this.top_category = top_category;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public List<String> getBrands() {
        return brands;
    }

    public void setBrands(List<String> brands) {
        this.brands = brands;
    }

    public List<String> getColours() {
        return colours;
    }

    public void setColours(List<String> colours) {
        this.colours = colours;
    }

    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    public List<String> getStyle() {
        return style;
    }

    public void setStyle(List<String> style) {
        this.style = style;
    }
    
    public Double getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Double priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Double getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Double priceTo) {
        this.priceTo = priceTo;
    }

    public List<Integer> getCluster() {
        return cluster;
    }

    public void setCluster(List<Integer> cluster) {
        this.cluster = cluster;
    }
}
