package com.gleb.dao.cassandra;

import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.gleb.dao.WishlistDAO;
import com.gleb.dao.objects.DBWishlist;
import com.gleb.dao.objects.DBWishlistItem;

/**
 * DAO object that handles Courses objects with new CQL cassandra approach.
 * 
 * @author viacheslav.vasianovych
 *
 */
@Repository
public class CQLWishlistsDAOImpl implements WishlistDAO {

    public static final String WISHLISTS_COLUMN_FAMILY = "wishlist_items";

    @Autowired
    private BasicCQLCassandraDAO basicCassandraDAO;
    
    private Mapper<DBWishlistItem> itemsMapper;

    @PostConstruct
    public void init() {
        MappingManager manager = new MappingManager(basicCassandraDAO.getSession());
        itemsMapper = manager.mapper(DBWishlistItem.class);
    }
    @Override
    public DBWishlist getWishlist(UUID userId) {
        Select.Where select = QueryBuilder.select().all().from(WISHLISTS_COLUMN_FAMILY).where(QueryBuilder.eq("user_id", userId));
        ResultSet resultSet = basicCassandraDAO.getSession().execute(select);
        if (resultSet.isExhausted()) {
            return null;
        }
        DBWishlist wishlist = new DBWishlist();
        wishlist.setId(userId);
        List<DBWishlistItem> items = itemsMapper.map(resultSet).all();
        Collections.sort(items, new Comparator<DBWishlistItem>() {
            @Override
            public int compare(DBWishlistItem o1, DBWishlistItem o2) {
                return (int) (o1.getDateAdded().getTime() - o2.getDateAdded().getTime());
            }
        });
        for (DBWishlistItem item : items) {
            if (item.getType().equals(CQLLikesDAO.LIKE_TYPE)) {
                wishlist.getLikedItems().add(item.getItemId());
            } else if (item.getType().equals(CQLLikesDAO.DISLIKE_TYPE)) {
                wishlist.getDislikedItems().add(item.getItemId());
            }
        }
        return wishlist;
    }

    @Override
    public void addItemToWishlist(UUID userId, String itemId, String type) {
        Insert insert = insertInto(WISHLISTS_COLUMN_FAMILY).value("item_id", itemId).value("user_id", userId).value("item_type", type)
                .value("date_added", new Date());
        basicCassandraDAO.getSession().execute(insert);
    }
}