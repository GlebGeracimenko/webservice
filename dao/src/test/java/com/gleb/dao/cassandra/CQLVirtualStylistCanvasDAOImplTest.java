package com.gleb.dao.cassandra;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gleb.dao.VirtualStylistCanvasDAO;
import com.gleb.dao.VirtualStylistItemDAO;

/**
 * Created by gleb on 31.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class CQLVirtualStylistCanvasDAOImplTest {

    @Autowired
    private VirtualStylistCanvasDAO itemDAO;

    @Autowired
    private VirtualStylistItemDAO virtualStylistItemDAO;

    @Autowired
    private BasicCQLCassandraDAO basicDAO;

    public void cleanUp() {
        basicDAO.getSession().execute("truncate " + CQLVirtualStylistCanvasDAOImpl.MATCH_CANVAS_TABLE);
    }

    @Before
    public void before() {
        cleanUp();
    }

    @Test
    public void test() {
    }
}
