package com.gleb.dao.elasticsearch;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;

import com.gleb.dao.objects.DBStore;
import com.gleb.dao.StoreDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by gleb on 27.11.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ElasticStoreDAOIT extends ElasticAbstractIT {

    @Autowired
    private StoreDAO dao;

    private List<DBStore> stores = new ArrayList<DBStore>();

    @Before
    public void before() {
        truncateType(ElasticStoreDAOImpl.STORE_TYPE);
    }

    @After
    public void after() {
        truncateType(ElasticStoreDAOImpl.STORE_TYPE);
    }

    private DBStore create(Long id, String value) {
        DBStore dbStore = new DBStore();
        dbStore.setId(UUID.randomUUID().toString());
        dbStore.setNetworkId("123");
        dbStore.setName(value);
        dbStore.setDescription(value);
        dbStore.setAddress(value);
        dbStore.setLatitude(value);
        dbStore.setLongitude(value);
        return dbStore;
    }

    private void addAll() {
        stores.add(create(1L, "Test1"));
        stores.add(create(2L, "Test2"));
        stores.add(create(3L, "Test3"));
        stores.add(create(4L, "Test4"));
        stores.add(create(5L, "Test5"));
    }

    private void check(DBStore store1, DBStore store2) {
        assertThat(store1.getId(), equalTo(store2.getId()));
        assertThat(store1.getNetworkId(), equalTo(store2.getNetworkId()));
        assertThat(store1.getName(), equalTo(store2.getName()));
        assertThat(store1.getDescription(), equalTo(store2.getDescription()));
        assertThat(store1.getAddress(), equalTo(store2.getAddress()));
        assertThat(store1.getLatitude(), equalTo(store2.getLatitude()));
        assertThat(store1.getLongitude(), equalTo(store2.getLongitude()));
    }

    private void save() {
        for (DBStore store : stores) {
            assertThat(dao.save(store), equalTo(store.getId()));
        }
    }

    private void getById() {
        for (DBStore store : stores) {
            check(dao.getById(store.getId()), store);
        }
    }

    private void getByName() throws InterruptedException {
        for (DBStore store : stores) {
            Thread.sleep(1000);
            check(dao.getByName(store.getName()), store);
        }
    }

    private void update() {
        for (DBStore store : stores) {
            store.setName("New name by test = " + store.getId());
            assertTrue(dao.update(store));
            check(dao.getById(store.getId()), store);
        }
    }

    @Test
    public void test() throws InterruptedException {
        addAll();
        save();
        getById();
        getByName();
        update();
    }

}
