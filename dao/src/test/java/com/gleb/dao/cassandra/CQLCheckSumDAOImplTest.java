package com.gleb.dao.cassandra;

import java.util.Date;
import java.util.UUID;

import com.gleb.dao.objects.DBCheckSum;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gleb.dao.CheckSumDAO;

/**
 * Created by gleb on 28.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
@Ignore
public class CQLCheckSumDAOImplTest {

    @Autowired
    private CheckSumDAO checkSumDAO;

    @Autowired
    private BasicCQLCassandraDAO basicDAO;

    public void cleanUp() {
        basicDAO.getSession().execute("truncate " + CQLLikesDAO.LIKES_COLUMN_FAMILY);
    }

    @Before
    public void before() {
        cleanUp();
    }

    @Test
    public void test() {
        DBCheckSum dbCheckSum = new DBCheckSum();
        dbCheckSum.setId(UUID.randomUUID());
        dbCheckSum.setName("Test");
        dbCheckSum.setBrand("Test");
        dbCheckSum.setTime(new Date());
        dbCheckSum.setMd5("Test");

        checkSumDAO.saveSum(dbCheckSum);

    }

}
