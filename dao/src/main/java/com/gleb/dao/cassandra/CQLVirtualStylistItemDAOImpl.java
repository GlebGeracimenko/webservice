package com.gleb.dao.cassandra;

import java.util.UUID;

import javax.annotation.PostConstruct;

import com.gleb.dao.VirtualStylistItemDAO;
import com.gleb.dao.objects.DBVirtualStylistCanvasItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

/**
 * Created by gleb on 31.10.15.
 */
@Repository
public class CQLVirtualStylistItemDAOImpl implements VirtualStylistItemDAO {

    public static final String MATCH_CANVAS_ITEMS = "match_canvas_items";

    @Autowired
    private BasicCQLCassandraDAO basicCassandraDAO;

    private Mapper<DBVirtualStylistCanvasItem> itemMapper;

    @PostConstruct
    public void init() {
        MappingManager manager = new MappingManager(basicCassandraDAO.getSession());
        itemMapper = manager.mapper(DBVirtualStylistCanvasItem.class);
    }

    @Override
    public UUID save(DBVirtualStylistCanvasItem stylistItem) {
        itemMapper.save(stylistItem);
        return stylistItem.getId();
    }

    @Override
    public boolean delete(UUID id) {
        if (id == null) {
            return false;
        }
        itemMapper.delete(id);
        return true;
    }

    @Override
    public DBVirtualStylistCanvasItem getById(UUID id) {
        return itemMapper.get(id);
    }

    @Override
    public DBVirtualStylistCanvasItem getBySKU(String sku) {
        Select.Where select = QueryBuilder.select().all().from(MATCH_CANVAS_ITEMS).where(QueryBuilder.eq("sku_code", sku));
        ResultSet resultSet = basicCassandraDAO.getSession().execute(select);
        if (resultSet.isExhausted()) {
            return null;
        }
        return itemMapper.map(resultSet).one();
    }
}
