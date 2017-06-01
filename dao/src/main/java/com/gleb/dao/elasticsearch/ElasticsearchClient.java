package com.gleb.dao.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ElasticsearchClient {

    private static final Logger log = Logger.getLogger(ElasticsearchClient.class);

    @Value("${elasticsearch.cluster.name}")
    private String clusterName;

    @Value("${elasticsearch.node.host}")
    private String initialNodeHost;

    @Value("${elasticsearch.node.port}")
    private int initialNodePort;

    @Value("${elasticsearch.node.index}")
    private String indexName;

    private Client client;

	@PostConstruct
    public void init() {
        log.info("Initializing Elasticsearch client. cluster name is " + clusterName);
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", clusterName).build();
        try {
            client = TransportClient.builder().settings(settings).build()
            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(initialNodeHost), initialNodePort));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public String getIndexName() {
        return indexName;
    }

    public Client getClient() {
        return client;
    }

    @PreDestroy
    public void destroy() {
        log.info("destroy elasticsearch client. clusterName: " + clusterName);

//        if (node != null)
//            node.close();
//        else
//            log.error("Elasticsearch node is NULL. cluster name is " + clusterName);
    }
}
