package com.gleb.dao.cassandra;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Select;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class CQLLikesDAOTest {

    @Autowired
    private CQLLikesDAO likesDAO;

    @Autowired
    private BasicCQLCassandraDAO basicDAO;

    public void cleanUp() {
        basicDAO.getSession().execute("truncate " + CQLLikesDAO.LIKES_COLUMN_FAMILY);
    }

    @Before
    public void before() {
        cleanUp();
    }

    @After
    public void after() {
        cleanUp();
    }

    @Test
    public void test() {
        String id = UUID.randomUUID().toString();
        Select.Where select = select().all().from( CQLLikesDAO.LIKES_COLUMN_FAMILY ).where(eq("product_id", id));
        ResultSet resultSet = basicDAO.getSession().execute(select);
        assertTrue(resultSet.isExhausted());
        UUID id1 = UUID.randomUUID();
        likesDAO.likeProduct("2", id1, new Date());
        resultSet = basicDAO.getSession().execute("select * from " + CQLLikesDAO.LIKES_COLUMN_FAMILY + " where product_id='2'");
        assertFalse(resultSet.isExhausted());
        List<Row> rows = resultSet.all();
        assertThat(rows.size(), equalTo(1));
        Row row = rows.get(0);
        assertThat(row.getString("type"), equalTo(CQLLikesDAO.LIKE_TYPE));
        assertThat(row.getUUID("user_id"), equalTo(id1));
        assertThat(row.getString("product_id"), equalTo("2"));

        likesDAO.dislikeProduct("2", id1, new Date());
        resultSet = basicDAO.getSession().execute("select * from " + CQLLikesDAO.LIKES_COLUMN_FAMILY + " where product_id='2'");
        assertFalse(resultSet.isExhausted());
        rows = resultSet.all();
        assertThat(rows.size(), equalTo(1));
        row = rows.get(0);
        assertThat(row.getString("type"), equalTo(CQLLikesDAO.DISLIKE_TYPE));
        assertThat(row.getUUID("user_id"), equalTo(id1));
        assertThat(row.getString("product_id"), equalTo("2"));
    }

    @Test
    public void test_likes_dislikes_Count() {
        String id = UUID.randomUUID().toString();
        Select.Where select = select().all().from( CQLLikesDAO.LIKES_COLUMN_FAMILY ).where(eq("product_id", id));
        ResultSet resultSet = basicDAO.getSession().execute(select);
        assertTrue(resultSet.isExhausted());

        likesDAO.likeProduct("2L", UUID.randomUUID(), new Date());
        likesDAO.likeProduct("2L", UUID.randomUUID(), new Date());
        likesDAO.likeProduct("2L", UUID.randomUUID(), new Date());
        likesDAO.likeProduct("2L", UUID.randomUUID(), new Date());
        likesDAO.likeProduct("2L", UUID.randomUUID(), new Date());
        
        likesDAO.likeProduct("3L", UUID.randomUUID(), new Date());
        likesDAO.likeProduct("3L", UUID.randomUUID(), new Date());
        likesDAO.likeProduct("3L", UUID.randomUUID(), new Date());
        
        Long product2Likes = likesDAO.getLikesCountForProduct("2L");
        Long product3Likes = likesDAO.getLikesCountForProduct("3L");
        
        likesDAO.dislikeProduct("2L", UUID.randomUUID(), new Date());
        likesDAO.dislikeProduct("2L", UUID.randomUUID(), new Date());
        likesDAO.dislikeProduct("2L", UUID.randomUUID(), new Date());
        likesDAO.dislikeProduct("2L", UUID.randomUUID(), new Date());
        
        likesDAO.dislikeProduct("3L", UUID.randomUUID(), new Date());
        likesDAO.dislikeProduct("3L", UUID.randomUUID(), new Date());
        likesDAO.dislikeProduct("3L", UUID.randomUUID(), new Date());
        likesDAO.dislikeProduct("3L", UUID.randomUUID(), new Date());
        
        Long product2Dislikes = likesDAO.getDislikesCountForProduct("2L");
        Long product3Dislikes = likesDAO.getDislikesCountForProduct("3L");
        
        assertThat(product2Likes, equalTo(5L));
        assertThat(product3Likes, equalTo(3L));
        
        assertThat(product2Dislikes, equalTo(4L));
        assertThat(product3Dislikes, equalTo(4L));
    }
}
