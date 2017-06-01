package com.gleb.dao.cassandra;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import com.gleb.dao.VirtualStylistCanvasDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.gleb.dao.objects.DBVirtualStylistCanvas;

/**
 * Created by gleb on 31.10.15.
 */
@Repository
public class CQLVirtualStylistCanvasDAOImpl implements VirtualStylistCanvasDAO {

    public static final String MATCH_CANVAS_TABLE = "match_canvas";

    @Autowired
    private BasicCQLCassandraDAO basicCassandraDAO;

    private Mapper<DBVirtualStylistCanvas> stylistCanvasMapper;

    @PostConstruct
    public void init() {
        MappingManager manager = new MappingManager(basicCassandraDAO.getSession());
        stylistCanvasMapper = manager.mapper(DBVirtualStylistCanvas.class);
    }

    @Override
    public UUID save(DBVirtualStylistCanvas virtualStylistCanvas) {
        stylistCanvasMapper.save(virtualStylistCanvas);
        return virtualStylistCanvas.getId();
    }

    @Override
    public boolean update(DBVirtualStylistCanvas virtualStylistCanvas) {
        if (virtualStylistCanvas == null) {
            return false;
        }
        save(virtualStylistCanvas);
        return true;
    }

    @Override
    public boolean delete(UUID id) {
        if (id == null) {
            return false;
        }
        stylistCanvasMapper.delete(id);
        return true;
    }

    @Override
    public DBVirtualStylistCanvas getById(UUID id) {
        DBVirtualStylistCanvas canvas = stylistCanvasMapper.get(id);
        return canvas;
    }

    @Override
    public List<DBVirtualStylistCanvas> getByUserId(UUID userId) {
        Select.Where select = select().all().from(MATCH_CANVAS_TABLE).where(eq("user_id", userId));
        ResultSet resultSet = basicCassandraDAO.getSession().execute(select);
        return stylistCanvasMapper.map(resultSet).all();
    }

    @Override
    public DBVirtualStylistCanvas getByIdAndUserId(UUID id, UUID userId) {
        Select.Where select = QueryBuilder.select().all().from(MATCH_CANVAS_TABLE).where(QueryBuilder.eq("id", id)).and(QueryBuilder.eq("userId", userId));
        ResultSet resultSet = basicCassandraDAO.getSession().execute(select);
        if (resultSet.getAvailableWithoutFetching() < 1) {
            return null;
        }
        return stylistCanvasMapper.map(resultSet).one();
    }

}
