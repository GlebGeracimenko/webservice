package com.gleb.dao.cassandra;

import com.gleb.dao.VirtualStylistCanvasDAO;
import com.gleb.dao.WishlistDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by gleb on 04.11.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class CQLWishlistsDAOImplTest {

    @Autowired
    private VirtualStylistCanvasDAO itemDAO;

    @Autowired
    private WishlistDAO wishlistDAO;

//    @Autowired
//    private CQLLikesDAO likesDAO;

    @Autowired
    private BasicCQLCassandraDAO basicDAO;

    public void cleanUp() {
        basicDAO.getSession().execute("truncate " + CQLWishlistsDAOImpl.WISHLISTS_COLUMN_FAMILY);
        basicDAO.getSession().execute("truncate " + CQLLikesDAO.LIKES_COLUMN_FAMILY);
    }

    @Before
    public void before() {
        cleanUp();
    }

    @Test
    public void test() {
    }
}
