package com.gleb.dao.elasticsearch;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import com.gleb.dao.objects.DBStyle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by gleb on 28.11.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ElasticStylesDAOIT extends ElasticAbstractIT {

    @Autowired
    private ElasticStylesDAOImpl dao;

    private List<DBStyle> styles = new ArrayList<DBStyle>();

    @Before
    public void before() {
        truncateType(ElasticStylesDAOImpl.STYLES_TYPE);
    }

    @After
    public void after() {
        truncateType(ElasticStylesDAOImpl.STYLES_TYPE);
    }

    private DBStyle create(String id, String value) {
        DBStyle dbStyle = new DBStyle();
        dbStyle.setId(id);
        dbStyle.setName(value);
        dbStyle.setDescription(value + "desc");
        return dbStyle;
    }

    private void addAll() {
        styles.add(create("1", "Test1"));
        styles.add(create("2", "Test2"));
        styles.add(create("3", "Test3"));
        styles.add(create("4", "Test4"));
        styles.add(create("5", "Test5"));
    }

    private void check(DBStyle style1, DBStyle style2) {
        assertThat(style1.getId(), equalTo(style2.getId()));
        assertThat(style1.getName(), equalTo(style2.getName()));
        assertThat(style1.getDescription(), equalTo(style2.getDescription()));
    }

    private void save() {
        for (DBStyle style : styles) {
            assertThat(dao.saveStyle(style), equalTo(style.getId()));
        }
    }

    private void getStyleById() {
        for (DBStyle style : styles) {
            check(dao.getStyleById(style.getId()), style);
        }
    }

    private void getStyleByName() throws InterruptedException {
        for (DBStyle style : styles) {
            Thread.sleep(1000);
            check(dao.getStyleByName(style.getName()), style);
        }
    }

    private void getStyles() {
        List<String> ids = new ArrayList<String>();
        for (DBStyle style : styles) {
            ids.add(style.getId().toString());
        }
        Collection<DBStyle> dbStyles = dao.getStyles(ids);
        Assert.assertNotNull(dbStyles);
        int count;
        for (DBStyle style : dbStyles) {
            if ((count = styles.indexOf(style)) > -1) {
                check(style, styles.get(count));
            }
        }
    }

    private void getAllStyles() {
        Collection<DBStyle> dbStyles = dao.getAllStyles();
        Assert.assertNotNull(dbStyles);
        int count;
        for (DBStyle style : dbStyles) {
            if ((count = styles.indexOf(style)) > -1) {
                check(style, styles.get(count));
            }
        }
    }

    @Test
    public void test() throws InterruptedException {
        addAll();
        save();
        getStyleById();
        getStyleByName();
        getStyles();
        getAllStyles();
    }

}
