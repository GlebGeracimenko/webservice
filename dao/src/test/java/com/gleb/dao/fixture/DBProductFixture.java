package com.gleb.dao.fixture;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import com.gleb.dao.objects.DBUser;
import com.gleb.dao.objects.DBProduct;

public class DBProductFixture {

    public static DBProduct create(String id) {
        DBProduct dbProduct = new DBProduct();
        dbProduct.setAvatar2Dready(true);
        dbProduct.setBrand("1");
        dbProduct.setCategory("top");
        dbProduct.setCluster(1L);
        dbProduct.setColor(Arrays.asList("red", "green"));
        dbProduct.setDesc("some desc");
        dbProduct.setGender(DBUser.GENDER_FEMALE);
        dbProduct.setId(id);
        dbProduct.setImages(Arrays.asList("http://somerwalink.com/image.jpg"));
        dbProduct.setImportedId("importedId");
        dbProduct.setName("some name");
        dbProduct.setPrice(1.0d);
        dbProduct.setResolvedImages(Arrays.asList("http://resolvedimage"));
        dbProduct.setSize(Arrays.asList("XL"));
        dbProduct.setSku("SKU");
        dbProduct.setStore("storeId");
        dbProduct.setStyle("casual");
        dbProduct.setTopCategory("top");
        dbProduct.setUrl("someurl");
        return dbProduct;
    }

    public static void check(DBProduct actual, DBProduct expected) {
        assertThat(actual.getBrand(), equalTo(expected.getBrand()));
        assertThat(actual.getCategory(), equalTo(expected.getCategory()));
        assertThat(actual.getCluster(), equalTo(expected.getCluster()));

        assertTrue(actual.getColor().containsAll(expected.getColor()));
        assertTrue(expected.getColor().containsAll(actual.getColor()));

        assertThat(actual.getDesc(), equalTo(expected.getDesc()));
        assertThat(actual.getGender(), equalTo(expected.getGender()));
        assertThat(actual.getId(), equalTo(expected.getId()));

        assertTrue(actual.getImages().containsAll(expected.getImages()));
        assertTrue(expected.getImages().containsAll(actual.getImages()));

        assertThat(actual.getImportedId(), equalTo(expected.getImportedId()));
        assertThat(actual.getName(), equalTo(expected.getName()));
        assertThat(Double.compare(actual.getPrice(), expected.getPrice()), equalTo(0));

        assertTrue(actual.getResolvedImages().containsAll(expected.getResolvedImages()));
        assertTrue(expected.getResolvedImages().containsAll(actual.getResolvedImages()));

        assertTrue(actual.getSize().containsAll(expected.getSize()));
        assertTrue(expected.getSize().containsAll(actual.getSize()));

        assertThat(actual.getSku(), equalTo(expected.getSku()));
        assertThat(actual.getStore(), equalTo(expected.getStore()));
        assertThat(actual.getStyle(), equalTo(expected.getStyle()));
        assertThat(actual.getTopCategory(), equalTo(expected.getTopCategory()));
        assertThat(actual.getUrl(), equalTo(expected.getUrl()));
    }
}
