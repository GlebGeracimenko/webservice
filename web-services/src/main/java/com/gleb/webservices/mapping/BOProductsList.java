package com.gleb.webservices.mapping;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.gleb.webservices.bo.BOProduct;

@XmlRootElement
public class BOProductsList {
    private List<BOProduct> products;

    public List<BOProduct> getProducts() {
        return products;
    }

    public void setProducts(List<BOProduct> products) {
        this.products = products;
    }
}
