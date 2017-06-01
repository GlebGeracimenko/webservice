package com.gleb.webservices.test.fixtures;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.gleb.webservices.bo.BOBrand;
import com.gleb.webservices.bo.BOProduct;

public class BrandFixture {
	public static BOBrand createBrand(String id) {
	    BOBrand brand = new BOBrand();
	    brand.setId(id);
	    brand.setDescription("some desc");
	    brand.setName("some brand name");
	    brand.setUrl("http://example.com");
	    return brand;
	}
	
	public static void check(BOBrand actual, BOBrand expected) {
		assertThat(actual.getId(), equalTo(expected.getId()));
		assertThat(actual.getName(), equalTo(expected.getName()));
		assertThat(actual.getUrl(), equalTo(expected.getUrl()));
		assertThat(actual.getDescription(), equalTo(expected.getDescription()));
	}
	
	public static void check(BOProduct actual, BOProduct expected) {
        assertThat(actual.getBrand(), equalTo(expected.getBrand()));
        assertThat(actual.getCategory(), equalTo(expected.getCategory()));
        assertTrue(actual.getColor().containsAll(expected.getColor()));
        assertTrue(expected.getColor().containsAll(actual.getColor()));
        assertThat(actual.getDesc(), equalTo(expected.getDesc()));
        assertThat(actual.getGender(), equalTo(expected.getGender()));
        assertThat(actual.getId(), equalTo(expected.getId()));
        assertTrue(actual.getImages().containsAll(expected.getImages()));
        assertTrue(expected.getImages().containsAll(actual.getImages()));
        assertThat(actual.getName(), equalTo(expected.getName()));
        assertThat(actual.getPrice(), equalTo(expected.getPrice()));
        assertTrue(actual.getSize().containsAll(expected.getSize()));
        assertTrue(expected.getSize().containsAll(actual.getSize()));
        assertThat(actual.getSku(), equalTo(expected.getSku()));
        assertThat(actual.getStore(), equalTo(expected.getStore()));
        assertThat(actual.getUrl(), equalTo(expected.getUrl()));
    }
}
