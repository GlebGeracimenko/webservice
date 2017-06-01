package com.gleb.dao.cassandra;

import java.util.UUID;

import javax.annotation.PostConstruct;

import com.gleb.dao.objects.DBCheckSum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.gleb.dao.CheckSumDAO;

/**
 * Created by gleb on 28.10.15.
 */
@Repository
public class CQLCheckSumDAOImpl implements CheckSumDAO {

    public static final String CHECK_SUM_TABLE = "check_sum";

    @Autowired
    private BasicCQLCassandraDAO basicCassandraDAO;

    private Mapper<DBCheckSum> dbCheckSumMapper;

    @PostConstruct
    public void init() {
        MappingManager manager = new MappingManager(basicCassandraDAO.getSession());
        dbCheckSumMapper = manager.mapper(DBCheckSum.class);
    }

    @Override
    public UUID saveSum(DBCheckSum dbCheckSum) {
        dbCheckSumMapper.save(dbCheckSum);
        return dbCheckSum.getId();
    }

}
