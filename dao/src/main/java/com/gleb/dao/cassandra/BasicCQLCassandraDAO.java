package com.gleb.dao.cassandra;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Basic cassandra bean that provides session for DB.
 * @author viacheslav.vasianovych
 *
 */
@Component
public class BasicCQLCassandraDAO {

    @Value("${cassandra.contactHost}")
    protected String[] contactHosts;

    @Value("${cassandra.keyspace}")
    protected String keyspace;

    private Cluster cluster;

    private Session session;

    @PostConstruct
    public void init() {
        // .withLoadBalancingPolicy(new DCAwareRoundRobinPolicy("US_EAST"))
        cluster = Cluster.builder().addContactPoints(contactHosts).build();
        // cluster.getConfiguration().getProtocolOptions().setCompression(ProtocolOptions.Compression.LZ4);
        session = cluster.connect(keyspace);
    }

    public Session getSession() {
        if (session == null || session.isClosed()) {
            synchronized (this) {
                session = cluster.connect(keyspace);
            }
        }
        return session;
    }
}
