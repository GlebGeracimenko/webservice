package com.gleb.dao.cassandra;

import static com.datastax.driver.core.querybuilder.QueryBuilder.delete;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.Select;
import com.gleb.dao.MappingDAO;

/**
 * Created by gleb on 26.09.15.
 */
@Repository
public class CQLMappingDAO implements MappingDAO {

    public static final String MAPPINGS_COLUMN_FAMILY = "mappings";

    @Autowired
    private BasicCQLCassandraDAO basicCassandraDAO;

    @Override
    public String getInternalName(String externalName, String type) {
        Session session = basicCassandraDAO.getSession();
        Statement statement = select("internal_name").from(MAPPINGS_COLUMN_FAMILY).where(eq("external_name", externalName.toLowerCase())).and(eq("type", type));
        ResultSet resultSet = session.execute(statement);
        if (resultSet.isExhausted()) {
            return null;
        }
        return resultSet.one().getString("internal_name");
    }

    @Override
    public void saveMapping(String externalName, String type, String internalName) {
        Insert insert = insertInto(MAPPINGS_COLUMN_FAMILY).value("external_name", externalName.toLowerCase()).value("internal_name", internalName).value("type", type);
        basicCassandraDAO.getSession().execute(insert);
    }

    @Override
    public List<String> getDistinct(String type) {
        Select.Where select = select().distinct().column("internal_name").from(MAPPINGS_COLUMN_FAMILY).allowFiltering().where(eq("type", type));
        ResultSet resultSet = basicCassandraDAO.getSession().execute(select);
        if (resultSet.isExhausted()) {
            return new ArrayList<String>();
        }
        List<String> result = new ArrayList<String>();
        for (Row row : resultSet.all()) {
            result.add(row.getString(0));
        }
        return result;
    }

    @Override
    public void deleteMapping(String externalName, String type, String internalName) {
        Delete.Where delete = delete().all().from(MAPPINGS_COLUMN_FAMILY).where(eq("external_name", externalName.toLowerCase())).and(eq("type", type))
                .and(eq("internal_name", internalName));
        basicCassandraDAO.getSession().execute(delete);
    }
}
