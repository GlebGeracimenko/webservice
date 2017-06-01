package com.gleb.webservices.bo;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by gleb on 26.09.15.
 */
@XmlRootElement
public class BOImportedProduct {

    private Long id;
    private String importedId;
    private String name;
    private String desc;
    private String url;
    private String brand;
    private String sku;
    private List<String> images;
    private double price;
    private List<String> size;
    private List<String> color;
    private String store;
    private String category;
    private String status;
    private int gender;
    private boolean resolveColour;
    private boolean resolveCategory;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImportedId() {
        return importedId;
    }

    public void setImportedId(String importedId) {
        this.importedId = importedId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isResolveColour() {
        return resolveColour;
    }

    public void setResolveColour(boolean resolveColour) {
        this.resolveColour = resolveColour;
    }

    public boolean isResolveCategory() {
        return resolveCategory;
    }

    public void setResolveCategory(boolean resolveCategory) {
        this.resolveCategory = resolveCategory;
    }

}
