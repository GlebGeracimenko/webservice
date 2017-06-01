package com.gleb.webservices.test.fixtures;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import com.gleb.dao.objects.DBProduct;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOProduct;

public class BOProductFixture {
	public static BOProduct createBOProduct(String id) {
		BOProduct boProduct = new BOProduct();
		boProduct.setBrand("101");
		boProduct.setCategory("some category");
		boProduct.setColor(Arrays.asList("color1", "color2"));
		boProduct.setDesc("some description");
		boProduct.setGender(DBUser.GENDER_FEMALE);
		boProduct.setId(id);
		boProduct.setImages(Arrays.asList("image1", "image2"));
		boProduct.setName("some name");
		boProduct.setPrice(123.123d);
		boProduct.setSize(Arrays.asList("XL", "L"));
		boProduct.setSku("123");
		boProduct.setStore("FC");
		boProduct.setUrl("some url");
		return boProduct;
	}
	
	public static void check(DBProduct actual, BOProduct expected) {
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
