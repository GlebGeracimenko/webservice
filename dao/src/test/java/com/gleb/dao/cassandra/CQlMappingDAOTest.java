package com.gleb.dao.cassandra;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gleb.dao.MappingDAO;

/**
 * Created by gleb on 27.09.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class CQlMappingDAOTest {

    @Autowired
    private MappingDAO mappingDAO;
 
    @Test
    public void test() {
//        mappingDAO.saveCategoryMapping("Test1Category1,category,Test1");
//        String resp = mappingDAO.getCategoryByMapping("Test1Category1");
//        Assert.assertThat(resp, equalTo("Test1"));
    }

}
