package com.gleb.webservices.mapping;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.gleb.dao.objects.DBProduct;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOProduct;

public class ProductMapperTest {

	@Test
	public void testFromElasticToBO_normalCase() {
		DBProduct dbProduct = new DBProduct();
		dbProduct.setBrand("some brand");
		dbProduct.setCategory("some category");
		dbProduct.setColor(Arrays.asList("color1", "color2"));
		dbProduct.setDesc("some description");
		dbProduct.setGender(DBUser.GENDER_FEMALE);
		dbProduct.setId("123L");
		dbProduct.setImages(Arrays.asList("image1", "image2"));
		dbProduct.setName("some name");
		dbProduct.setPrice(123.123d);
		dbProduct.setSize(Arrays.asList("XL", "L"));
		dbProduct.setSku("123");
		dbProduct.setStore("FC");
		dbProduct.setUrl("some url");
		
		ProductMapper mapper = new ProductMapper();
		mapper.init();
		
		BOProduct mappedProduct = mapper.map(dbProduct);
		
		assertThat(mappedProduct.getBrand(), equalTo(dbProduct.getBrand()));
		assertThat(mappedProduct.getCategory(), equalTo(dbProduct.getCategory()));
		assertTrue(mappedProduct.getColor().containsAll(dbProduct.getColor()));
		assertTrue(dbProduct.getColor().containsAll(mappedProduct.getColor()));
		assertThat(mappedProduct.getDesc(), equalTo(dbProduct.getDesc()));
		assertThat(mappedProduct.getGender(), equalTo(dbProduct.getGender()));
		assertThat(mappedProduct.getId(), equalTo(dbProduct.getId()));
		assertTrue(mappedProduct.getImages().containsAll(dbProduct.getImages()));
		assertTrue(dbProduct.getImages().containsAll(mappedProduct.getImages()));
		assertThat(mappedProduct.getName(), equalTo(dbProduct.getName()));
		assertThat(mappedProduct.getPrice(), equalTo(dbProduct.getPrice()));
		assertTrue(mappedProduct.getSize().containsAll(dbProduct.getSize()));
		assertTrue(dbProduct.getSize().containsAll(mappedProduct.getSize()));
		assertThat(mappedProduct.getSku(), equalTo(dbProduct.getSku()));
		assertThat(mappedProduct.getStore(), equalTo(dbProduct.getStore()));
		assertThat(mappedProduct.getUrl(), equalTo(dbProduct.getUrl()));
	}
	
	@Test
	public void testFromBOtoElastic_normalCase() {
		BOProduct boProduct = new BOProduct();
		boProduct.setBrand("some brand");
		boProduct.setCategory("some category");
		boProduct.setColor(Arrays.asList("color1", "color2"));
		boProduct.setDesc("some description");
		boProduct.setGender(DBUser.GENDER_FEMALE);
		boProduct.setId("123L");
		boProduct.setImages(Arrays.asList("image1", "image2"));
		boProduct.setName("some name");
		boProduct.setPrice(123.123d);
		boProduct.setSize(Arrays.asList("XL", "L"));
		boProduct.setSku("123");
		boProduct.setStore("FC");
		boProduct.setUrl("some url");
		
		ProductMapper mapper = new ProductMapper();
		mapper.init();
		
		DBProduct mappedProduct = mapper.map(boProduct);
		
		assertThat(mappedProduct.getBrand(), equalTo(boProduct.getBrand()));
		assertThat(mappedProduct.getCategory(), equalTo(boProduct.getCategory()));
		assertTrue(mappedProduct.getColor().containsAll(boProduct.getColor()));
		assertTrue(boProduct.getColor().containsAll(mappedProduct.getColor()));
		assertThat(mappedProduct.getDesc(), equalTo(boProduct.getDesc()));
		assertThat(mappedProduct.getGender(), equalTo(boProduct.getGender()));
		assertThat(mappedProduct.getId(), equalTo(boProduct.getId()));
		assertTrue(mappedProduct.getImages().containsAll(boProduct.getImages()));
		assertTrue(boProduct.getImages().containsAll(mappedProduct.getImages()));
		assertThat(mappedProduct.getName(), equalTo(boProduct.getName()));
		assertThat(mappedProduct.getPrice(), equalTo(boProduct.getPrice()));
		assertTrue(mappedProduct.getSize().containsAll(boProduct.getSize()));
		assertTrue(boProduct.getSize().containsAll(mappedProduct.getSize()));
		assertThat(mappedProduct.getSku(), equalTo(boProduct.getSku()));
		assertThat(mappedProduct.getStore(), equalTo(boProduct.getStore()));
		assertThat(mappedProduct.getUrl(), equalTo(boProduct.getUrl()));
	}
}

