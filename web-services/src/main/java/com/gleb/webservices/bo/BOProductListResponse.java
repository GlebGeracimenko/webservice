package com.gleb.webservices.bo;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BOProductListResponse {
	private int count;
	private final List<BOProduct> products = new LinkedList<BOProduct>();
	private final Map<String, BOBrandWithFollowing> brands = new HashMap<String, BOBrandWithFollowing>();

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<BOProduct> getProducts() {
		return products;
	}

	public void addProducts(Collection<BOProduct> products) {
		this.products.addAll(products);
	}

	public Map<String, BOBrandWithFollowing> getBrands() {
		return brands;
	}

	public void addBrands(Collection<BOBrandWithFollowing> brandsList) {
		for (BOBrandWithFollowing boBrand : brandsList) {
			brands.put(boBrand.getId(), boBrand);
		}
	}
}
