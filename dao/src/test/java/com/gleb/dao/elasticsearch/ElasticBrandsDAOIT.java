package com.gleb.dao.elasticsearch;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gleb.dao.objects.DBBrand;

/**
 * Created by gleb on 26.11.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ElasticBrandsDAOIT extends ElasticAbstractIT{

    @Autowired
    private ElasticBrandsDAOImpl brandsDAO;

    private List<DBBrand> brands = new ArrayList<>();

    @Before
    public void before() {
        truncateType(ElasticBrandsDAOImpl.BRAND_TYPE);
    }

    @After
    public void after() {
        truncateType(ElasticBrandsDAOImpl.BRAND_TYPE);
    }

    private DBBrand create(String id, String value) {
        DBBrand dbBrand = new DBBrand();
        dbBrand.setId(UUID.randomUUID().toString());
        dbBrand.setName(value);
        dbBrand.setDescription(value);
        dbBrand.setUrl("www." + value + ".com.ua");
        return dbBrand;
    }

    private void addAll() {
        brands.add(create("1L", "Test1"));
        brands.add(create("2L", "Test2"));
        brands.add(create("3L", "Test3"));
        brands.add(create("4L", "Test4"));
        brands.add(create("5L", "Test5"));
    }

    private void check(DBBrand brand, DBBrand brand1) {
        assertThat(brand.getId(), equalTo(brand1.getId()));
        assertThat(brand.getName(), equalTo(brand1.getName()));
        assertThat(brand.getDescription(), equalTo(brand1.getDescription()));
        assertThat(brand.getUrl(), equalTo(brand1.getUrl()));
    }

    private void save() {
        for (DBBrand brand : brands) {
            assertThat(brandsDAO.saveBrand(brand), equalTo(brand.getId()));
        }
    }

    private void getById() {
        for (DBBrand brand : brands) {
            DBBrand brand1 = brandsDAO.getBrand(brand.getId());
            check(brand, brand1);
        }
    }

    private void getBrandByName() throws InterruptedException {
        for (DBBrand brand : brands) {
            Thread.sleep(1000);
            check(brandsDAO.getBrandByName(brand.getName()), brand);
        }
    }

//    private void getBrandByDescription() throws InterruptedException {
//        for (DBBrand brand : brands) {
//            Thread.sleep(1000);
//            check(brandsDAO.getBrandByDescription(brand.getDescription()), brand);
//        }
//    }

    private void getBrands() throws InterruptedException {
        List<String> ids = new ArrayList<String>();
        for (DBBrand brand : brands) {
            ids.add(brand.getId());
        }
        Collection<DBBrand> dbBrands = brandsDAO.getBrands(ids);
        Assert.assertNotNull(dbBrands);
        int count = 0;
        for (DBBrand brand : dbBrands) {
            Thread.sleep(1000);
            if ((count = brands.indexOf(brand)) >= 0)
                check(brand, brands.get(count));
        }
    }

    private void getAllBrands() throws InterruptedException {
        List<DBBrand> dbBrands = brandsDAO.getAllBrands();
        Assert.assertNotNull(dbBrands);
        int count = 0;
        for (DBBrand brand : dbBrands) {
            Thread.sleep(1000);
            if ((count = brands.indexOf(brand)) >= 0)
                check(brand, brands.get(count));
        }
    }

    @Test
    public void test() throws InterruptedException {
        addAll();
        save();
        getById();
        getBrandByName();
        //getBrandByDescription();
        getBrands();
        getAllBrands();
    }


}
