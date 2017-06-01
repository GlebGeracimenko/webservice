package com.gleb.dao.elasticsearch;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteAction;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticAbstractIT {

    @Autowired
    private ElasticsearchClient clientHolder;

    private static Logger logger = LoggerFactory.getLogger(ElasticAbstractIT.class);

    public void truncateType(String type) {
        // We need to wait the sync for elastic. it is async - so just dirty hack
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SearchRequestBuilder requestBuilder = clientHolder.getClient().prepareSearch(clientHolder.getIndexName()).setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(QueryBuilders.matchAllQuery()).setNoFields();
        SearchResponse response = requestBuilder.setExplain(true).execute().actionGet();
        SearchHits hits = response.getHits();
        if (hits.getTotalHits() > 0) {
            BulkRequestBuilder bulk = clientHolder.getClient().prepareBulk();
            for (SearchHit hit : hits) {
                DeleteRequestBuilder builder = new DeleteRequestBuilder(clientHolder.getClient(), DeleteAction.INSTANCE);
                builder.setId(hit.getId());
                builder.setIndex(clientHolder.getIndexName());
                builder.setType(type);
                bulk.add(builder.request());
            }
            bulk.get();
        }
    }

    protected void storeBatchElastic(String type, String batchName) throws IOException {
        File file = new File("src/test/resources/test-batches", batchName);
        logger.info("Start batching: " + batchName + ", type: " + type);
        ObjectMapper jsonMapper = new ObjectMapper();
        JavaType javaType = jsonMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class);
        List<JsonNode> batchObjects = jsonMapper.readValue(file, javaType);
        for (JsonNode json : batchObjects) {
            IndexResponse response = clientHolder.getClient().prepareIndex().setSource(json.toString()).setType(type).setIndex(clientHolder.getIndexName())
                    .get();
            logger.info("\tindexed object: id " + response.getId() + ", source: " + json.toString());
        }
    }
}
