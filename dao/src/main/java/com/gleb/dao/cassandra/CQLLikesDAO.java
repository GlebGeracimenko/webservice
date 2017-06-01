package com.gleb.dao.cassandra;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

import java.util.Date;
import java.util.UUID;

import com.gleb.dao.LikesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

@Repository
public class CQLLikesDAO implements LikesDAO {
    public static final String LIKE_TYPE = "LIKE";
    public static final String DISLIKE_TYPE = "DISLIKE";
    public static final String LIKES_COLUMN_FAMILY = "likes_dislikes";

    @Autowired
    private BasicCQLCassandraDAO basicDAO;

    @Override
    public void likeProduct(String productId, UUID userId, Date time) {
        Insert insert = insertInto(LIKES_COLUMN_FAMILY).value("product_id", productId).value("user_id", userId).value("time", time).value("type", LIKE_TYPE);
        Batch batch = QueryBuilder.batch(insert);
        basicDAO.getSession().execute(batch);
    }

    @Override
    public void dislikeProduct(String productId, UUID userId, Date time) {
        Insert insert = insertInto(LIKES_COLUMN_FAMILY).value("product_id", productId).value("user_id", userId).value("time", time).value("type", DISLIKE_TYPE);
        Batch batch = QueryBuilder.batch(insert);
        basicDAO.getSession().execute(batch);
    }

    @Override
    public Long getLikesCountForProduct(String productId) {
        Select.Where select = select().countAll().from(LIKES_COLUMN_FAMILY).where(eq("product_id", productId)).and(eq("type", LIKE_TYPE));
        ResultSet resultSet = basicDAO.getSession().execute(select);
        return resultSet.one().getLong(0);
    }

    @Override
    public Long getDislikesCountForProduct(String productId) {
        Select.Where select = select().countAll().from(LIKES_COLUMN_FAMILY).where(eq("product_id", productId)).and(eq("type", DISLIKE_TYPE));
        ResultSet resultSet = basicDAO.getSession().execute(select);
        return resultSet.one().getLong(0);
    }
}
